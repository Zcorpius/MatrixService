# MatrixService

演示两个独立 App 通过 **bindService + AIDL** 实现跨进程通信（IPC）。

## v0.1 架构与设计

### 模块结构

```
MatrixService/
├── matrixserver/     # Server 端 App (com.neo.matrixserver)
├── matrixclient/     # Client 端 App (com.neo.matrixclient)
└── app/              # （未启用）
```

两个模块各自编译为独立的 APK，运行在不同进程，通过 AIDL 定义的接口跨进程调用。

### 通信模型

```
┌───────────────────┐    bindService + AIDL    ┌───────────────────┐
│   MatrixClient    │ ──────────────────────▶  │   MatrixServer    │
│   (Client 进程)    │                          │   (Server 进程)    │
│                   │                          │                   │
│  MatrixClient     │    IMatrixProxy          │  MatrixServer     │
│  Service          │◀───────Binder──────────▶│  Service          │
│                   │   getState() / setState() │                   │
└───────────────────┘                          └───────────────────┘
```

**通信方式：bindService + AIDL**
- Server 端通过 `onBind()` 返回 `IMatrixProxy.Stub` 实例
- Client 端通过 `bindService()` + `ServiceConnection` 获取 `IBinder`，再用 `IMatrixProxy.Stub.asInterface()` 转为代理对象
- 绑定过程经 AMS 中转撮合，绑定完成后 Client 与 Server 点对点 Binder IPC 通信

### AIDL 接口

```java
// IMatrixProxy.aidl（Server 和 Client 各持有一份相同的 AIDL 文件）
interface IMatrixProxy {
    int getState();
    void setState(int state);
}
```

### 各模块职责

#### MatrixServer（服务提供方）

| 类 | 职责 |
|---|---|
| `MatrixServerService` | 核心服务，持有 `currentState`，实现 `IMatrixProxy.Stub`，在 `onBind()` 中返回 Binder |
| `MainViewModel` | 控制 `startService()` / `stopService()` |
| `MainActivity` | UI 入口，提供启动/停止服务按钮 |

#### MatrixClient（服务调用方）

| 类 | 职责 |
|---|---|
| `MatrixClientService` | 通过显式 Intent `bindService()` 绑定到 `MatrixServerService`，持有 `IMatrixProxy` 代理对象，封装 `requestGetState()` / `requestSetState()` |
| `MainViewModel` | 调用 `MatrixClientService` 提供的方法，驱动 UI 状态更新 |
| `MainActivity` | UI 入口，提供启动/获取状态/设置状态/停止按钮 |

### 关键实现细节

1. **显式 Intent 绑定** — Client 通过 `ComponentName("com.neo.matrixserver", "MatrixServerService")` 精确绑定目标服务
2. **`<queries>` 声明** — Android 11+（targetSdk 30+）默认包不可见，Client 的 Manifest 中需声明 `<queries><package android:name="com.neo.matrixserver" /></queries>`
3. **Service `exported="true"`** — Server 端的 Service 需设置为 exported 以允许其他 App 绑定
4. **静态实例引用** — `MatrixClientService.INSTANCE` 供 `MainViewModel` 直接访问 Service 实例
5. **`BIND_AUTO_CREATE`** — Client 绑定时带此 flag，Server 进程未运行时自动拉起
