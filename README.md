# BookGrab

## 思路
创建书信息实体类Book和动态代理需要使用的代理信息类ProxySource，Book实体类拥有书名(name)、作者(author)等私有属性。ProxySource实体类拥有IP地址(ip),端口号(port)等私有属性。

创建Util工具类包含导出excel方法(exprotExcel)，代理IP方法(proxyIP),获取代理服务器列表类(getProxyList)等

建立页面加载线程类LoadPageThread和排序进程类SortBooksThread，页面加载线程类调用jsoup第三方包对页面进行处理，如果是列表页面则添加新任务加载符合要求的书详细信息页面，如果是书详细信息页面则使用Util工具类解析页面(parseBook)。

main方法开启五个线程进行页面加载、一个排序线程对获得的书队列进行取出排序，设置页面加载标志(loadFlag)和排序标志(sortFlag)进行控制线程停止，当页面加载线程发现页面加载完毕时将加载标志置为false，主进程检测到加载标志变更调用关闭线程方法(executorService.shutdown()),当五个线程全部关闭时将排序标志置为false，排序线程检测到标志变更关闭自身。

主类采用线程池(ExecutorService)管理页面加载线程，使用线程安全队列(LinkedBlockingQueue)进行信息的缓存，并启用一个单独线程进行书队列的排序和管理。当服务器返回403时启用动态代理，使用预先加载的代理服务器列表来对请求进行代理。

##算法

排序线程采用堆排序算法来对书列表进行排序和添加，相比于冒泡排序、直接插入排序算法拥有更低的时间复杂度。

## 用时

不使用代理情况下33280毫秒 ，采用代理时因代理服务器不稳定事件不可控。  