import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class CaptureBasic extends JPanel {
	private BufferedImage mImg;
	private BufferedImage mat2BI(Mat mat) {
		int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
		byte[] data = new byte[dataSize];
		mat.get(0, 0, data);
		int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
		if (type == BufferedImage.TYPE_3BYTE_BGR) {
			for (int i = 0; i < dataSize; i += 3) {
				byte blue = data[i + 0];
				data[i + 0] = data[i + 2];
				data[i + 2] = blue;
			}
		}
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}
	@Override
	public void paintComponent(Graphics g) {
		if (mImg != null) {
			g.drawImage(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(), this);
		}
	}
	public static void main(String[] args) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			Mat capImg = new Mat();
			VideoCapture capture = new VideoCapture(1);
			int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
			int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
			if (height == 0 || width == 0) {
				throw new Exception("camera not found!");
			}
			JFrame frame = new JFrame("camera");
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			CaptureBasic panel = new CaptureBasic();
			//			panel.addMouseListener(new MouseAdapter() {
			//				@Override
			//				public void mouseClicked(MouseEvent arg0) {
			//					System.out.println("click");
			//				}
			//				@Override
			//				public void mouseMoved(MouseEvent arg0) {
			//					System.out.println("move");
			//				}
			//				@Override
			//				public void mouseReleased(MouseEvent arg0) {
			//					System.out.println("mouseReleased");
			//				}
			//				@Override
			//				public void mousePressed(MouseEvent arg0) {
			//					System.out.println("mousePressed");
			//				}
			//				@Override
			//				public void mouseExited(MouseEvent arg0) {
			//					System.out.println("mouseExited");
			//					//System.out.println(arg0.toString());
			//				}
			//				@Override
			//				public void mouseDragged(MouseEvent arg0) {
			//					System.out.println("mouseDragged");
			//					//System.out.println(arg0.toString());
			//				}
			//			});
			frame.setContentPane(panel);
			frame.setVisible(true);
			frame.setSize(width + frame.getInsets().left + frame.getInsets().right, height + frame.getInsets().top + frame.getInsets().bottom);
			int n = 0;
			Mat temp = new Mat();
			while (frame.isShowing() && n < 500) {
				//System.out.println("第"+n+"张");
				capture.read(capImg);
				Imgproc.cvtColor(capImg, temp, Imgproc.COLOR_RGB2GRAY);
				//Imgcodecs.imwrite("G:/opencv/lw/neg/back"+n+".png", temp);
				panel.mImg = panel.mat2BI(detectFace(capImg));
				panel.repaint();
				//n++;
				//break;
			}
			capture.release();
			frame.dispose();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("例外：" + e);
		}
		finally {
			System.out.println("--done--");
		}
	}
	/**
	 * opencv实现人脸识别
	 */
	public static Mat detectFace(Mat img) throws Exception {
		System.out.println("Running DetectFace ... ");
		// 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器，该文件位于opencv安装目录中
		//CascadeClassifier faceDetector = new CascadeClassifier("E:\\aaadowncode\\opencv401\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
		String filePath = "E:\\aaadowncode\\opencv401\\sources\\data\\haarcascades\\";
		String filename0 = "haarcascade_frontalcatface.xml";
		String filename1 = "haarcascade_frontalcatface_extended.xml";
		String filename2 = "haarcascade_frontalface_alt.xml";
		String filename3 = "haarcascade_frontalface_alt_tree.xml";
		String filename4 = "haarcascade_frontalface_alt2.xml";
		String filename5 = "haarcascade_frontalface_default.xml";
		CascadeClassifier faceDetector = new CascadeClassifier(filePath + filename5);
		// 在图片中检测人脸
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDetections);
		//System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		Rect[] rects = faceDetections.toArray();
		if (rects != null && rects.length >= 1) {
			for (Rect rect : rects) {
				Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
			}
		}
		return img;
	}
	/**
	 * opencv实现人型识别，hog默认的分类器。所以效果不好。
	 */
	public static Mat detectPeople(Mat img) {
		//System.out.println("detectPeople...");
		if (img.empty()) {
			System.out.println("image is exist");
		}
		HOGDescriptor hog = new HOGDescriptor();
		hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());
		System.out.println(HOGDescriptor.getDefaultPeopleDetector());
		//hog.setSVMDetector(HOGDescriptor.getDaimlerPeopleDetector());
		MatOfRect regions = new MatOfRect();
		MatOfDouble foundWeights = new MatOfDouble();
		//System.out.println(foundWeights.toString());
		hog.detectMultiScale(img, regions, foundWeights);
		for (Rect rect : regions.toArray()) {
			Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
		}
		return img;
	}
}