# Share2

Share2 利用了 Android 系统的原生 API 实现了分享功能，支持文本信息、图片、音视频等其他类型文件的分享，兼容处理了不同系统版本下的常见问题，并在一些主流应用上进行了兼容测试。

| 系统兼容 | 版本号 |
|:----:|------|
| 最低 | API 11 |
| 最高 | API 27 |

> 如果发现 Share2 存在不兼容的情况，请在 Issues 中提出。

### 实现效果

<img src="https://upload-images.jianshu.io/upload_images/580515-360aa823c6d64335.png" width="50%" height="50%">

[Demo 下载](https://github.com/baishixian/Share2/releases/download/0.9.3/demo.apk)

### 相关文章：

[利用 Android 系统原生 API 实现分享功能](https://www.jianshu.com/p/1d4bd2c5ef69) 和
[利用 Android 系统原生 API 实现分享功能（2）](https://www.jianshu.com/p/a950f5596a01)

## 添加项目依赖

**在 Project 的 build.gradle 下添加 jcenter 仓库**

```
repositories {
    jcenter()
}
```

文件分享前必须要先获得存储权限，对于系统 API 22 及以上的设备请先动态获取存储授权，Share2 内部不会主动请求权限。

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

**在 Module 的 build.gradle 下添加 Share2 依赖**

```
compile 'gdut.bsx:share2:0.9.3'
```

## 使用 Share2 进行分享

### 分享一张图片

```
new Share2.Builder(this) 
      // 指定分享的文件类型
      .setContentType(ShareContentType.IMAGE)
      // 设置要分享的文件 Uri
      .setShareFileUri(shareImageUri)
      // 设置分享选择器的标题
      .setTitle("Share Image")
      .build()
      // 发起分享
      .shareBySystem();
```

#### Share2 支持多种类型的内容分享，你可以通过 ` setContentType(type)` 来进行设置。分享文件时可以调用`setShareFileUri` 方法来设置文件的 `Uri`。

> **⚠️注意** 此 `Uri` 必须是系统 `ContentProvider` 返回的，而不能是自定义 `FileProvider` 返回的 `Uri`，不然会造成其他第三方应用无法识别，比如QQ、微信等应用会提示获取不到文件内容的问题就是由此造成。

相关内容可参考我的文章：[利用 Android 系统原生 API 实现分享功能（2）](https://www.jianshu.com/p/a950f5596a01)

#### 对于只知道文件路径的情况，Share2 提供了 `FileUtil.getFileUri()` 方法来帮助你获取对应文件的 `Uri`：

```
// FileUtil.Class
public static Uri getFileUri (Context context, @ShareContentType String shareContentType, File file)
```

> 如果不知道文件类型，参数 `shareContentType` 可以直接传入 `null`。

## 其他一些使用场景

### 分享其他类型的内容

**分享文本内容时：**

```
new Share2.Builder(this)
    .setContentType(ShareContentType.TEXT)
    // 设置要分享的文本内容
    .setTextContent("This is a test message.")
    .setTitle("Share Text")
    .build()
    .shareBySystem();
```

**分享未知类型的文件时：**

```
new Share2.Builder(this)
    .setContentType(ShareContentType.FILE)
    .setShareFileUri(fileUri)
    .build()
    .shareBySystem();

```

### 指定分享到特定的应用

某些场景下需要分享到特定的应用，可以使用下面这个方法：

```
/**
 * componentPackageName 指定接收分享的 APP 的包名
 * componentClassName 指定接收分享的 APP 的类名
 */
 public Builder setShareToComponent(String componentPackageName, String componentClassName);
```

比如当我们要分享到微信朋友圈时：
 
```
builder.setShareToComponent("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
```

### 设置分享时是否强制使用选择器

如果系统设置了某些类型文件的默认打开方式，当我们进行分享时会直接跳转到默认应用。调用下面的方法可以设置每次分享时是否都要显示选择器，即使系统已经有默认的打开方式了。

```
// 默认是 true
public Builder forcedUseSystemChooser (boolean enable)
```

### 设置分享完成后的回调

通过下面的方法可以设置分享完成后在当前分享页 Activity 的 `onActivityResult` 方法中接收回调：

```
public Builder setOnActivityResult (int requestCode)
```
