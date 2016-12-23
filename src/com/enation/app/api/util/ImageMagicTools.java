package com.enation.app.api.util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

/**
 * 
 * ImageMagick和im4java处理图片
 * 
 * @author sunlightcs
 * 
 *         2011-6-1
 * 
 *         http://hi.juziku.com/sunlightcs/
 */
public class ImageMagicTools {

	/**
	 * 
	 * ImageMagick的路径
	 */
	// public static String imageMagickPath = null;
	static {
		/**
		 * 
		 * 获取ImageMagick的路径
		 */
		// Properties prop = new PropertiesFile().getPropertiesFile();
		// linux下不要设置此值，不然会报错
		// imageMagickPath = prop.getProperty("imageMagickPath");
	}
	
	/**
	 * 判断系统类型
	 * @return
	 */
	public static boolean isWin(){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			return true;
		}
		return false;
	}
	
	/**
	 * 设置ImageMagick的路径
	 */
	public static void searchImageMagicPath(ConvertCmd convert){
		if(isWin()){
			convert.setSearchPath("C:\\Program Files\\ImageMagick-6.8.8-Q16");
		}
	}

	/**
	 * 
	 * 根据坐标裁剪图片
	 * 
	 * @param srcPath   要裁剪图片的路径
	 * @param newPath   裁剪图片后的路径
	 * @param x         起始横坐标
	 * @param y         起始挫坐标
	 * @param x1                结束横坐标
	 * @param y1                结束挫坐标
	 * @param newWidth    压缩后的宽度
	 */
	public static void cutImage(String srcPath, String newPath, int x, int y, int x1,	int y1,Integer newWidth) throws Exception {
		int width = x1 - x;
		int height = y1 - y;
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.crop(width, height, x, y);
		op.quality(0.75);
		op.density(72);
		op.resize(newWidth);
		op.strip();
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		searchImageMagicPath(convert);
		convert.run(op);
	}

	/**
	 * 
	 * 根据尺寸缩放图片
	 * @param width             缩放后的图片宽度
	 * @param height            缩放后的图片高度
	 * @param srcPath           源图片路径
	 * @param newPath           缩放后图片的路径
	 */
	public static void cutImage(int width, int height, String srcPath,	String newPath) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, height);
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		searchImageMagicPath(convert);
		convert.run(op);

	}

	/**
	 * 根据宽度缩放图片
	 * 
	 * @param width            缩放后的图片宽度
	 * @param srcPath          源图片路径
	 * @param newPath          缩放后图片的路径
	 */
	public static void cutImage(int width, String srcPath, String newPath)	throws Exception {
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width,null,">");
		op.quality(0.75);
		op.density(72);
		op.strip();
		op.addImage(newPath);
		ConvertCmd convert = new ConvertCmd();
		searchImageMagicPath(convert);
		convert.run(op);
	}
	

	/**
	 * 给图片加水印
	 * @param srcPath            源图片路径
	 */
	public static void addImgText(String srcPath) throws Exception {
		IMOperation op = new IMOperation();
		op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8")
				.draw("text 5,5 www.tcardz.com");
		op.addImage();
		op.addImage();
		ConvertCmd convert = new ConvertCmd();
		searchImageMagicPath(convert);
		convert.run(op, srcPath, srcPath);
	}

	
	/** 
	* 计算旋转参数 
	*/  
	public static Rectangle CalcRotatedSize(Rectangle src,int angel){  
	    // if angel is greater than 90 degree,we need to do some conversion.  
	    if(angel > 90){  
	        if(angel / 9%2 ==1){  
	            int temp = src.height;  
	            src.height = src.width;  
	            src.width = temp;  
	        }  
	        angel = angel % 90;  
	    }  
	      
	    double r = Math.sqrt(src.height * src.height + src.width * src.width ) / 2 ;  
	    double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
	    double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;    
	    double angel_dalta_width = Math.atan((double) src.height / src.width);    
	    double angel_dalta_height = Math.atan((double) src.width / src.height);    
	  
	    int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha    
	            - angel_dalta_width));    
	    int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha    
	            - angel_dalta_height));    
	    int des_width = src.width + len_dalta_width * 2;    
	    int des_height = src.height + len_dalta_height * 2;    
	    return new java.awt.Rectangle(new Dimension(des_width, des_height));    
	}  
	
	/** 
	* 读取指定图片 
	*/  
	public static BufferedImage getPicture(String filePath) {  
	    BufferedImage bi = null;  
	    try{  
	        File file = new File(filePath);  
	        if(!file.exists()){  
	            return null;  
	        }  
	        bi = ImageIO.read(file);  
	    } catch (Exception e){  
	        e.printStackTrace();  
	    }  
	    return bi;  
	}  
	
	public static void main(String[] args) throws Exception {
		String fullName = "E:/201611210209550347.png";
		String smallImg = "E:/sd-small.jpg";
		cutImage(1240,fullName,"E:/a-1240.jpg");
		cutImage(800,fullName,"E:/a-800.jpg");
		cutImage(600,fullName,"E:/a-600.jpg");
//		copyRightImage("E:\\orginal-201610251606522935.jpg", "E:\\123.jpg");
//		ImageMagickCreator creator = new ImageMagickCreator("E:/201611210209550347.png", "E:/sd-small.jpg");
//		creator.resize(200);
		System.out.println("test");
	}
}