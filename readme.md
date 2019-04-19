1.官方下载 https://opencv.org/ 找到 Releases 下载 windows版本 最好 sources也下载一下  下载之后尽量保存 官网一直在更新 老版本不确定什么时候就找不到了
2.下载后执行exe文件后找到build-java  找到jar包
3.人脸特征文件
// 1 读取OpenCV自带的人脸识别特征XML文件
		//CascadeClassifier facebook=new CascadeClassifier(                  "E:\\OpenCV-4.0.0\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
		CascadeClassifier facebook=new CascadeClassifier("E:\\aaadowncode\\opencv401\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
4.配合本代码 扩展开发