浪费了很多时间看 tls，还没搞明白 java 里怎么用 tls，什么是 tls

wireshark 怎么抓 tls 的包，怎么解密

x509 的 wikipedia



这几天不干别的了 ，搞懂所有 tls ssl 相关的技术，包括 https，rsa 什么东西？

ssl 基本过程：

* 客户端索要公钥
* 双方协商生成对话密钥
* 双方采用对话秘钥进行加密通信

​    前两步称为握手 handshake

握手详细过程，Diffie-Hellman 算法

* 浏览器给出协议版本号、一个客户端生成的随机数、客户端支持的加密方法
* 服务器确认双方使用的加密算法，tls 版本号和一个随机数，给出数字证书、一个是用 dh 算法生成的随机数如 pubkey？
* 浏览器获取 pubkey，算出另一个 pubkey？发过去
* 浏览器返回一个 session ticket

ssl/tls 在传输层上封装了应用层数据，可以在不许修改应用层协议前提下，给不安全的应用层协议提供一定安全保障

```
client: clienthello
server: serverhello
	certificate 这步发证书
	serverkeyexchange 这个发选择的加密套件生成的公钥
	serverhellodone
client: clientkeyexchange 发送自己生成的公钥
	changecipherspec 变更密码规范，告知对方以后通信基于 aes 加密
	finished
server: changecipherspec
	finihsed

然后 client 开始发数据

如果服务端想验证客户端身份，发一个 certificate request
一般不用认证
```

ip.addr eq a.b.c.d and tls 过滤关心的包，妙啊

```
client: random bytes 28 字节随机数
	session id 新连接 0
	cipher suites
	copmression methods
	application layer protocol negotiation 应用层协议，干啥的？
	signature_algorithms 签名算法

server: random bytes
	cipher suite
	compression method
	
	cipher suite 说一下，比如
		TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
		
		TLS tsl 协议
		ECDHE 对称秘钥交换算法
		RSA 公私钥算法
		WITH 分隔符
		AES_256_GCM 对称加密算法
		SHA_384 哈希算法

	certificate
		一个 CA 签发的证书
		一个 CA 链证书
	
		根据 CA 链判断证书真伪
		
		服务器证书里有一个公钥，用于加密后面生成的 premaster secret
		
	server key exchange
		EC Diffie-Hellman Server Params
		Pubkey xxx
			这个随机数加上前面的两个随机数构成最终会话秘钥

client: client key exchange
	浏览器收到 certificate 后 dh 算法生成 pubkey，发给服务器
	两者交换 pubkey，分别生成了一样的 sessionkey

	change cipher spec
		变更密码规范协议，只是一个通知，表名以后加密通信

	encrypted handshake message
		客户端使用生成的对话秘钥，加密之前所有收发握手消息的 hash 和 mac 发给服务端，服务端使用相同会话秘钥解密消息，校验 hash 和 mac
			
	change cipher spec 和 encrypted handshake 和 client hello、server hello 不同，后两者封装在 handshake protocol，前两者和 handshake protocol 一样在 tls record layer 层

server: change cipher spec 服务端也发送 change cipher spec 表明它也加密方式发送数据
	服务端使用会话秘钥加密之前所有收发握手消息的 hash 和 mac，发给客户端校验
	如果客户端校验成功，握手完成，双方按照 ssl 规范使用协商生成的会话秘钥加密发送数据
	
```

通信满足以下特征认为是安全的

* 机密性，别人看不到
* 完整性
* 身份认证
* 不可否认



ssl/tls 让 http 运行在安全的 ssl/tls 上，收发报文不再 socket api 而是调用专门安全接口

只要学会 ssl/tls https 手到擒来

ssl 在 osi 五层，会话层，网景公司 94 年发明，v2 v3 俩版本

v3 时 ssl 被 ietf 改为 tls，传输层安全，正式标准化，版本号从 1.0 开始

其实 tls1.0 就是 sslv3.1



tls 06 年 1.1 08 年 1.2 18 年 1.3，每个版本紧跟密码学的发展和互联网现状，持续强化安全性能，成了信息安全领域中的权威标准



目前应用最广泛的 tls 是 1.2，1.1,1.0 ，sslv3/v2 都被认为不安全



tls 由记录协议、握手协议、警告协议、变更密码规范协议、扩展协议等几个子协议组成，总和使用了对称加密、非对称加密、身份认证等许多密码学前沿技术



浏览器、服务器使用 tls 建立连接时，选择一组恰当的加密算法来实现安全通信，这些算法组合成为加密套件

> cipher suite



tls 密码套件命名非常规范，格式很固定，基本形式：秘钥交换算法+签名算法+对称加密算法+摘要算法

比如`ECDHE-RSA-AES256-GCM-SHA384`

含义是 `握手使用 ECDHE，签名和身份认证使用 RSA，握手后通信使用 AES 秘钥长度 256，分组模式 GCM，摘要算法 SHA384 用于消息认证和产生随机数`



##### openssl

著名的开源密码学程序库和工具包，几乎支持所有公开加密算法和协议，已经成为了事实标准，许多应用软件使用它作为底层库实现 tls 功能，包括 apache nginx

来自另一个库 ssleay，曾考虑用命 opentls，当时 tls 没确立，ssl 早为人知，故选用 openssl

openssl 三个版本 1.0.2 1.1.0，这两个 19 年不再维护，最新长期支持版本 1.1.1

当前所有 tls 的 rfc 文档末尾数字都是 46

ssl/tls 可以承载除 http 外其它应用协议如 ftp -> ftps??? ldap -> ldaps

openssl 密码套件定义和 tls 不一样，tls 是 tls_ecdhe_rsa_with_aes_256_gcm_sha384，前缀 tls，中间 with 分隔握手和通信算法

nss 是另一个著名的开源密码库，mozilla 开发



> *https 大潮无法阻挡，死守 http 会被冲刷到互联网角落里*

https 额外 cpu 成本 <1%，额外网络成本 <2%，和 http 相差无几

现在云厂商提供一键申请、价格低廉证书，还有 let's encrypt 这个颁发免费证书的 CA

https 关联了密码学、tls、pki 等许多领域，不是几个月几周能精通的（我去你妈的！）

可惜实施 https 不需要完全掌握这些，只需抓住少数几个点



1. 申请证书

   大公司选择向 digicert globalsign 这种 CA 申请，贵；中小网站 let's encrypt 足矣，效果不次

   lets encrypt 一直在推动证书自动化部署，为此实现了专门的 ACME 协议，rfc8555，很多客户端软件可以完成申请、验证、下载、更新一条龙操作，如 certbot、acme.sh，都在 let's encrypt 网站上有，相关文档详细

   ==几分钟就能完成申请==

   申请证书，同时申请 RSA 和 ECDSA 两种证书，nginx 配置双证书验证，这样服务器可以自动选择快速的椭圆曲线证书，同时兼容只支持 RSA 客户端

   如果 RSA 证书，私钥至少 2048 位，摘要算法应该选 SHA-2 如 SHA256 SHA384

   证书只有 90 天，时间一到就失效，应该定期更新；可以 crontab 每周或每月任务更新

   很多 ACME 客户端会自动添加这样的定期任务

2. 配置 https

   配置 web 服务器，443 端口开启 https

   nginx 上加个 

    ```nginx
    listen 443 ssl;
    
    ssl_certificate xxx_rsa.crt; #rsa2048 cert
    ssl_certificate_key xxx_rsa.key; #rsa2048 private key
    
    ssl_certificate xxx_ecc.crt; #ecdsa cert
    ssl_certificate_key xxx_ecc.key; #ecdsa private key
    
    ssl_protocols TLSv1.2 TLSv1.3; 强制只支持 1.2 以上协议
    
    ssl_session_timeout 5m;
    ssl_session_tickets on; 打开 session ticket 会话复用，干啥的？
    ssl_session_ticket_key ticket.key;
    
    ssl_prefer_server_ciphers on; 以服务器套件优先
    ssl_ciphers ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-CHACHA20-POLY1305:ECDHE+AES128:!MD5:!SHA1;
    	这波是只使用 ECDHE AES 和 CHACHA20，向 tls1.3 看齐
    ```

   如果你用了谷歌的 boringssl，可以开启一个等价的密码套件

   ```nginx
   ssl_ciphers [ECDHE-ECDSA-AES128-GCM-SHA256|ECDHE-ECDSA-CHACHA20-POLY1305]
   嘛意思呢？这些套件允许客户端优先选择，如果客户端硬件没优化，服务器顺着客户端意思，优先选择和 AES 等价的 chacha20 算法，让客户端快点
   ```

   这些配完了，访问 ssllab.com 网站测测网站安全度

   3. 配置 https 服务器还有个虚拟主机问题要解决，http 协议里多个域名可以在一个 ip 上跑，就是虚拟主机

      web 服务器使用请求头里的 host 来选择

      https 里，请求头只有 tls 握手之后才发送，握手时必须选择虚拟主机的正数，tls 无法得知域名信息，只能用 ip 来区分

      所以最早，每个 https 域名必须独立 ip 地址，很不方便

​			  此时得用到 tls 扩展，给协议加 sni，server name indication 补充条款，作用类似 host

​              client hello 带上域名信息，这样服务器根据名字而不是 ip 选择证书

````wireshark
Extension: server_name (len=19)
	Server Name Indication extension
		Server Name Type: host_name (0)
		Server Name: www.chrono.com
````

nginx 很早基于 sni 特性支持 https 虚拟主机，openresty 里还可以编写 lua 脚本，利用 redis、mysql 等更灵活快速地加载证书

4. 重定向

   旧 http 不可丢弃，需要 http 网址用 301 或 302 重定向到 https 网站，nginx 里

   ```nginx
   return 301 https://$host$request_uri;
   rewrite ^ https://$host$request_uri permanent; 永久重定向
   ```

   重定向 1 增加网络成本，多一次请求，2 响应可能被中间人篡改实现会话劫持，跳转到恶意网站

   可以 hsts，http strict transport security，http 严格传输安全技术来消除，https 服务器需要在响应头增加

   `strict-transport-security`

   ```http
   Strict-Transport-Security: max-age=11111111; includeSubDomains
   ```

   告诉浏览器，半年内不许用 http，以后自己动，别给我发了

   浏览器收到 hsts 后访问相同域名会自动把 uri http 改成 https，让中间人失去机会

   可以在 nginx 里

   ````nginx
   add_header Strict-Transport-Security max-age=15768000; 半年
   ````

   HSTS 无法防止第一次黑客攻击，chrome 有个 HSTS preload 列表，域名在这个列表的，何时都会强制 https

   5. 大型网站全站 https 还需要很多细枝末节的工作，如 csp，content security policy 的各种指令和标签配置安全策略，使用反向代理卸载 ssl

      啥意思？？什么是 csp 和 卸载 ssl

如果要支持老 xp ie6，可以选择开启 sslv3、rsa、rc4、sha-1

​	何不直接拒绝访问



访问 http 协议时可以看到请求头：`Upgrade-Insecure-Requests: 1`

这是 CSP 协议的一种，表示浏览器升级到 https 协议



**https 慢**

对称加密 aes chacha20 都很快，还有硬件优化，报文传输性能损耗可以忽略，所以慢是握手慢，会消耗 2-rtt

除了握手的网络耗时，还有

- 产生用于秘钥交换的临时公钥对 ECDHE
- 验证证书时访问 CA 获取 CRL 或 OCSP
- 非对称加密解密，处理 pre-master

不做任何优化，https 简历连接可能比 http 慢几百毫秒、几秒，有网络耗时、计算耗时，让人感觉慢

过去式

现在有很多行之有效的 https 优化手段，可以把连接额外耗时降低到几十毫秒甚至 0



```
>client hello
<server hello
<certificate
<产生临时密钥对
<server key exchange
<server hello done
>验证证书
>产生临时秘钥
>计算 pre-master
>client key exchange
>change cipher spec
>finished
<计算 pre-master
<change cipher spec
<finishied
```

*硬件优化*

花钱。https 是计算密集型不是 io 密集型，网卡、带宽、ssd 南辕北辙，应该选更牛逼的 cpu，最好内建 AES 优化，可以加速握手和传输

可以选择 ssl 加速卡，加解密调用其 api，让专门的硬件做非对称加解密，分担 cpu 计算压力

​     加速卡升级慢、支持算法有限、不能灵活定制解决方案

​     那你说个蛇？

可以专门的服务器集群来彻底卸载 TLS 握手时的加密计算

*软件优化*

升级内核从 2.x 到 4.x，一般不好做的

尽量 tls 1.3，简化握手过程，完全握手只需要 1-rtt，而且更安全

如果只能 1.2，握手秘钥交换协议应该选椭圆曲线的 ECDHE，更快，更安全，支持 false start，能把握手时间从 2-rtt 降为 1-rtt，达到和 tls1.3 类似效果

椭圆曲线也要选择性能高的曲线，最好 x25519，次优是 P-256

对称加密算法，可以选 AES_128_GCM，比 AES_256_GCM 快一丢丢

nginx 用下面指令配置服务器密码套件和椭圆曲线，把优先使用的放前面

```nginx
ssl_ciphers TLS13-AES-256-GCM-SHA384:TLS13-CHACHA20-POLY1305-SHA256:EECDH+CHACHA20;
ssl_ecdh_curve X25519:P-256;
```

*证书优化*

证书验证较为耗时，服务器需要把自己证书链全发给客户端，客户端接收后逐一验证

证书传输、证书验证可以优化

服务器证书可以选椭圆曲线 ECDSA 而不是 RSA，因为 224 位 ECC 相对于 2048 的 RSA，前者更小，节约带宽和运算量

客户端证书验证其实很复杂，除了公钥解密验证多个证书签名，还得访问 CA 看证书是不是撤销失效，下载 CRL 或 OCSP，又产生了 DNS、建立连接、收发数据等一系列网络通信，增加多个 RTT



CRL，certificate revocation list，证书吊销列表，由 CA 定期发布，里面是所有被撤销信任的证书序号，查询这个列表就能知道证书是否有效，但是定期发布，就有时间窗口隐患，而且证书越来越多，列表增大，一个 CRL 上 MB。每次预先下载几 MB 无用数据才能连接网站，实用性低

CRL 基本不用了。



OCSP，online certificate status protocol，在线正数状态协议，向 CA 发送查询请求，让 CA 返回正数有效状态

但是有一次网络请求，还依赖 CA，如果 CA 忙也不行



又出来一个 OCSP Stapling，OCSP 装订，可以让服务器预先访问 CA 获取 OSCP 响应，然后握手时随着证书一起发给客户端，免去客户端连接 CA 服务器查询时间

​    听起来很扯，服务端不会篡改响应吗？

 

*会话复用*

https 建立连接是三次握手加 tls 握手，这 tls 握手重点是算 master secrete 主秘钥，它每次都重新计算，考虑重用，就叫会话复用，tls session resumption，类似 http cache，也是提高 https 性能大杀器

> resumption <正式>重新开始，恢复；<法律>（土地、权利等的）重获

会话复用有两种

- session id，双方各自保存会话 id，内存里存储主秘钥和其它相关信息；客户端再次连接发一个 id 过来，服务端内存找，有则直接用主秘钥恢复会话状态，跳过证书验证和秘钥交换，一个 rtt 建立安全通信

​       后面几次 server hello 完直接 change cihper spec 和 finished 复用会话完成握手

​      这个是最早出现的会话复用技术，应用最广，缺点是 server 存储压力大

- session ticket

  类似 http cookie，客户端自己存，server 加密会话信息，用 new session ticket 发给客户端

  重连，客户端用扩展 session_ticket 发送 ticket 而不是 sesion id，server 解密，验证有效期，恢复会话

  但是 sesion ticket 需要使用一个固定的秘钥文件，ticket_key 来加密 ticket，为了防止秘钥被破解，保证向前安全，秘钥文件需要定期轮换，比如一小时或一天

- 预共享密钥

​       false start、session id、session ticket 只能实现 1-rtt，tls 1.3 实现了 0-rtt，原理类似 session ticket，但发送 ticket 同时会带上应用数据 early data，免去 1.2 里的服务器确认步骤，叫 pre-shared key，简称 PSK

​       PSK 为了效率牺牲一些安全，容易受到重放攻击威胁，黑客截取 PSK 数据，像复读机一样反复向服务器发送

​       解决办法是只允许 GET/HEAD，在消息加时间戳、nonce 验证或一次性票证，限制重放



​		草他妈不清不楚的，什么是预共享密钥？



小结，ECDHE 密码条件好，快，还可以 false start；服务端应该开始 OCSP Stapling 避免客户端验证 CA；会话复用类似 cache，前提是客户端之前必须成功建立连接，后面可以 session id、session ticket 等凭据跳过秘钥交换、证书验证等，直接开始加密通信



SSL 加速卡不是吹牛逼，阿里 tengine 基于 intel qat 加速卡定制了 nginx 和 openssl

ocsp 会增额外网络连接成本故Chrome 只对 ev 证书使用 ocsp，普通网站 dv、ov 证书省略此操作

nginx，ssl_stapling on 开启 scsp，openresty 可以 lua 灵活定制

session id session ticket 在 tls1.3 废弃，只能 psk 实现会话复用



秘钥长度单位是 bit 不是 byte，秘钥长度 128 其实是 16 B，1024 是 128 B



对称加密，加密解密秘钥是同一个，是对称的；只要保证了秘钥的安全，整个通信过程就具有了机密性

tls 里有很多对称加密算法，RC4 DES 3DES AES CHACHA20 ，前三种都不安全，禁止（很好），现在常用 AES 和  CHACHA20

AES，高级加密标准，advanced encryption standard，秘钥长度可以是 128、192、256

他是 DES 替代者，安全度高，性能好，有的硬件还会特殊优化，很流行，应用最广泛

chacha20 是谷歌设计的另一种加密算法，长度固定位 256，纯软件运行性能比 aes 好，曾经在移动端很流行

armv8 后也加入了 aes 硬件优化，所以现在 chacha20 不有明显优势了，但仍然不赖



*加密分组模式*

对称算法有个`分组模式`的概念，可以让算法用固定长度的秘钥加密任意长度的明文

最早有 ECB CBC CFB OFB 等，都有漏洞，现在都不用了，最新分组模式叫做 AEAD，authenticated encryption with associated data，用关联数据认证加密？？，加密同时增加了认证，常用 GCM，CCM，Poly305



组合对称加密和加密分组模式，就得到 tls 密码套件中定义的对称加密算法了

比如 AES128-GCM，ChaCha20-Poly1305



*非对称加密*

对称加密问题在于如何把秘钥安全地传递给对方，即密钥交换

对称加密算法无法解决秘钥交换的问题，所以有了非对称加密

它有两个，public key 和 private key



非对称加密很难，TSL 只有少数几种，DH DSA RSA ECC

RSA 最著名，几乎可以说是非对称加密的代名词，它的安全基于`整数分解`数学难题，使用两个超大素数的成绩作为生成秘钥的材料，想要从公钥算出私钥非常困难

10 年前 RSA 秘钥推荐长度 1024 现在翻倍

ECC 是后起之秀，elliptic curve cryptography，基于椭圆曲线离散对数数学难题，使用特定曲线方程和基点生成公私钥，子算法 ECDHE 用于秘钥交换，ECDSA 用于数字签名

目前比较常用的两个曲线是 P-256，secp256r1，openssl 称为 prime256v1 和 x25519，这是 NIST 和 NSA 推荐使用的曲线，x25519 是最安全最快的曲线



ECC 椭圆会引起误解，其实它的曲线不是椭圆，只是方程很类似计算椭圆周长公式，实际形状更像抛物线

比起 RSA，ECC 在安全强度和性能上都有明显优势，160 位 ECC 相当于 1024 位 RSA，224 ECC 相当于 2048 RSA

秘钥短了，计算量、消耗内存带宽也少了，加解密性能上去了



非对称加密和对称加密比，性能差几个数量级，只用它不实用

```
1000 次加密解密
aes_128_cbc 0.97ms
rsa_1024 138.59ms
rsa_2048 840.35ms
```



加密算法有块加密 block cipher 和流加密，stream cipher，DES AES 是块加密，RC4 chacha20 是流加密

ECC 虽然定义了公私钥，但不能直接实现秘钥交换和身份认证，需要搭配 DH DSA 等形成专门的 ECDHE ECDSA

RSA 特殊，本身支持秘钥交换，也支持身份认证

比特币、以太坊等区块链技术也用 ECC，secp256k1



由于密码学界不信任 NIST 和 NSA 怀疑 secp 系列曲线有潜在的弱点，所以研究出了 x25519 来源于曲线方程的参数 `2^255 - 19`，令一个高强度曲线 x448，参数是 `2^448 - 2^224 - 1`



linux 可以 openssl 命令行工具测试算法加密解密速度，如

 `openssl speed aes` 

`openssl speed rsa2048`

TSL1.2 要求必须实现 TLS_RSA_WITH_AES_128_CBC_SHA

TSL1.3 要求必须实现 TLS_AES_128_GCM_SHA256

​	废除了秘钥交换算法 DH 和 DSA



**前面混合加密解决了机密性，还需要完整性、身份认证**

完整性靠摘要算法，digest algorithm，也就是散列函数、哈希函数

摘要算法可以理解成特殊压缩算法，可以把任意长度数据压缩成固定长度、独一无二的摘要字符串，好像生成了一个数字指纹

可以把摘要算法理解成特殊单向加密算法，只有算法没有秘钥，加密后无法解密

摘要算法实际上把数据从大空间映射到小空间，所以存在冲突可能性，好的摘要算法必须能抵抗冲突，让可能性小



摘要算法有单向性、雪崩效应，输入的微小改动导致输出剧烈变化，所以 tls 用来生成随机数 PRF，pseudo random function



常用的有 MD5，message-digest 5；SHA-1，secure hash algorithm 1，能生成 16 - 20 字节长度的数字摘要

但是安全性较低，tls 里禁用了



tls 推荐 SHA-1 的后代 SHA-2

SHA-2 是一系列摘要算法的统称，6 种，SHA224 SHA256 SHA384 这三种较为常用，分别对应 28 32 48 字节摘要



摘要算法保证数字摘要和原文完全等价，所以只要原文后附上摘要就能保证数据完整性



但是摘要算法没机密性，如果明文传输，黑客可以修改消息，改摘要，网站不知道

所以完整性必须建立在机密性之上，混合加密系统里用会话秘钥加密消息和摘要，这样黑客无法得知明文



有个术语叫 HMAC，哈希消息认证码

> 明文+摘要用会话秘钥加密，服务端解密，然后 SHA-2 求个摘要，再比对



*数字签名*

加密+摘要，通信过程比较安全了，但是通信端点还有问题。

私钥+摘要算法能实现数字签名，完成身份认证和不可否认

由于非对称加密效率低，所以私钥只加密原文的摘要，得到的签名小，方便保管和传输

签名和公钥完全公开，任何人可以获取，但是只有私钥对应的公钥才能解开，拿到摘要，比对原文验证完整性，可以像签署文件一样证明消息确实是你发的

​	你指的是服务器



**签名和验签**

你和网站交换公钥就可以用签名和验签确认消息真实性



**公钥的信任**

谁都能发布公钥，还缺少防止黑客伪造公钥的手段。如何判断这个公钥是ok 的？

找公认可信第三方，作为信任的起点，构建公钥信任链

这个第三方就是 CA，给公钥签名，用自身信誉保证公钥无法伪造，是可信的



CA 对公钥的签名认证格式包含序列号、用途、颁发者、有效时间和自己公钥，打包，签名，完整证明公钥关联的各种信息，形成证书 certificate



著名 CA 屈指可数，digicert verisign entrust let's-encrypt，证书分三种 DV OV EV，区别在于可信度

DV 最低，域名级别可信，背后是谁不知道；

EV 最高，经过法律和审计的严格喝茶，可以证明网站拥有者的身份，浏览器地址栏会显示出公司的名字，如 apple github



签名是啥？私钥加密原文的摘要就是签名，你拿它公钥解密，再把内容求个摘要看是否一样，看看一不一样



root ca 自证，self-signed certificate，根证书，你得相信



os 和 browser 内置各大 CA 证书，上网时 server 发过来它证书，可以验证证书签名，顺着证书链 certificate chain 一层层验证，找到根证书，可以确定证书是可信的，从而公钥也是可信的



PKI public key infrastructure 目前是网络的安全基础设施，但绝对的安全不存在，它也有弱点，信任

- CA 失误或被欺骗，发错了

- CA 被黑了

情况 1 用 CRL 证书吊销列表，certificate revocation list 和 OCSP onlinecertificate status protocol 来处理

情况 2 只能从 os 或 browser 下手，撤销对 CA 的信任，列入黑名单，这样它颁发的证书都被认为不安全



公钥的分发需要使用数字证书，必须 CA 信任链验证，否则不可信



摘要算法其它用途：散列表、数据校验、大文件比较



SHA-2 很安全，但是现在有了 SHA-3，还未纳入 TLS



账号+密码 在安全通信未建立前容易被窃取，tls 里不用



证书格式遵循 X509 v3 标准，两种编码方式，二进制 DER 和 ascii 的 PEM



==我理解的验签过程：==

> 证书链验证大致是这么个流程：一口气发过来好几个证书，从第一个（最低级的是第一个）往上找，比如三级二级一级，一级证书里有签发者，找找浏览器里有没有这个签发者，如果没有：不安全；如果有，拿浏览器里的签发者的公钥解密这个一级证书里的签名，然后用一级证书里的算法对证书求个摘要，比较，如果一样，一级证书验签完成，安全；
>
> 然后用一级证书的公钥去验签二级证书，然后是三级证书



证书含有公钥和其它信息，都是公开的，由 CA 签名保证完整性，篡改即失效，所以可以随便发布

证书把网站的身份和公钥绑定在一起，验证了其私钥，也就验证了它

>再说一下我的理解：
>
>一个证书里有公钥，这个是主要的，还有一个签名，签名没在证书里，但是和证书在一块
>
>怎么证明这个证书是你？靠数字签名
>
>这个签名相当于一个凭证，是它的上一级证书签发的，内容是上一级证书的拥有者用它的私钥对当前证书的摘要的加密结果
>
>然后一级一级直到根证书，根证书是可信的，它的证书是自签名的，用自己的私钥加密自己的摘要

证书格式就是可怕的 X509



你输入 uri，浏览器先提取协议名、域名，dns 解析域名得到 ip，三次握手建立 tcp 连接

由于是 https 还有一次握手，基于 tcp 建立安全连接



**握手过程**

TLS 包含几个子协议，可以理解为由几个不同职责的模块组成

- 记录协议 record protocol 规定了 TLS 收发数据基本单位，记录 record，像 tcp 里的 segment，所有其它子协议需要通过记录协议发出；多个记录数据可以在一个 tcp 包里，不需要 ack
- 警报协议 alert protocol 向对方发出警报，像 http 响应码。比如 protocol_version 是不支持旧版本，bad_certificate 是证书不行，收到警报后另一方可以继续或终止连接
- 握手协议 handshake protocol 是 TLS 最复杂的子协议，比 TCP SYN/ACK 复杂的多。浏览器和服务器握手时协商 TLS 版本号、随机数、密码套件等，然后交换证书和秘钥，最终协商得到会话秘钥
- 变更密码规范协议 change cipher spec protocol 很简单只是一个通知，告诉对方后续数据都将加密保护；在它之前数据都是明文的

最多两次 rtt 建立连接

```
> tls version
> client random
> cipher suites
< tls version
< server random
< cipher suite ECDHE_RSA 比如
< certificate
< server key exchange, named curve, pubkey
< server hello done
> client key exchange, pubkey
> change cipher spec
> finished
< change cipher spec
< finished
```

为了更好分析 TLS 握手过程，可以让浏览器导出握手过程中的秘密信息，让 wireshark 解密

> 增加系统变量  SSLKEYLOGFILE 设置浏览器日志文件路径
>
> wireshark 设置 protocol-TLS，在 pre-master-secret log filename 写刚才文件
>
> 过滤器选 tcp port 443

ECDHE 椭圆秘钥交换！

