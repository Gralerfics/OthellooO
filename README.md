# OthellooO
大学 Java 课的期末作业 Project，黑白棋。

存档管理、玩家管理、悔棋、回放之类的，没写联机、个性化主题什么的（玩家主题色可以改改 xs）。

---

框架用的 LWJGL，用 Java 写图形其实还是挺怪的（咳）。重写片段着色器，在 glsl 里写光追。

GPU 光线追踪参考: https://blog.csdn.net/weixin_44176696/article/details/119044396

BRDF 参考: https://blog.csdn.net/weixin_44176696/article/details/119791772

片段着色器核心部分其实基本是照着写，权作入门学习。

优化也是照着写，甚至可能没写好（笑），需要点电脑配置；设置面板有不少简化选项，用以降低配置需求（主要是我自己跑不起来）。

---

Hdr 读取用了: https://github.com/Ivelate/JavaHDR

Hdr 图片来自: https://polyhaven.com/hdris

---

Swing 也是头一次用，稍微重写了一下 ScrollPane 和一些输入框什么的。

---

第一次用 Java / Maven 写比较大的程序，可能也是最后一次（咳），代码写得很乱，就是这么一传，没指望有人改hhhh。

AI 棋手部分来自 @Maystern，分级代表迭代搜索深度。

---

一些图：

<div align="center"><img src="display/20211227 1.png" width="500"></div>

<div align="center"><img src="display/20211227 2.png" width="500"></div>

<div align="center"><img src="display/20211208 Test hdr 1.jpg" width="250">&nbsp &nbsp<img src="display/20211117 Test LargeWindow.jpg" width="250"></div>

<div align="center"><img src="display/frame 1.png" width="250">&nbsp &nbsp<img src="display/frame 2.png" width="250"></div>
