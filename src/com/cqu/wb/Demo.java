/**
 * @description 中科院分词系统(NLPIR)测试类
 */
package com.cqu.wb;

import java.math.BigDecimal;

import com.sun.jna.Native;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//初始化
		CLibrary cLibrary = (CLibrary) Native.loadLibrary(System.getProperty("user.dir")+"\\source\\NLPIR", CLibrary.class);
		//NLPIR_Init():初始化分析器，并根据配置文件为NLPIR准备数据
		int init_flag = cLibrary.NLPIR_Init("", 1, "0");
		String resultString;
		if(init_flag == 0) {
			resultString = cLibrary.NLPIR_GetLastErrorMsg();
			System.out.println("初始化失败!\n" + resultString);
			return;
		}
		
		String inputString = "哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服，"  
                + "整晚失眠会连累我嘛，他虽然是只猴子，但你也不能这样对他啊，官府知道会说我虐待动物的，"  
                + "说起那个金刚圈，啊~去年我在陈家村认识了一个铁匠，他手工精美，价钱又公道，童叟无欺，"  
                + "干脆我介绍你再定做一个吧！";
		
		try {
			//分词后每个词后面都跟着词性标记（bPOSTagged参数为1时，输出结果显示标记；为0时，不现实标记），并彼此以空格分隔
			//NLPIR_ParagraphProcess():处理一个段落文字，返回运行结果缓存的指针
			resultString = cLibrary.NLPIR_ParagraphProcess(inputString, 1);
			System.out.println("分词结果：\n" + resultString);
			
			//NLPIR_AddUserWord():向用户词典添加一个词汇
			cLibrary.NLPIR_AddUserWord("金刚圈");
			cLibrary.NLPIR_AddUserWord("左宽右窄");
			resultString = cLibrary.NLPIR_ParagraphProcess(inputString, 1);
			System.out.println("\n增加用户词典后分词结果为：\n" + resultString);
			
			//NLPIR_DelUsrWord():从用户词典中删除一个词汇
			cLibrary.NLPIR_DelUsrWord("左宽右窄");
			resultString = cLibrary.NLPIR_ParagraphProcess(inputString, 1);
			System.out.println("\n删除用户词典后分词结果为：\n" + resultString);
			
			//NLPIR_ImportUserDict():从文件中导入用户自定义的词典
			cLibrary.NLPIR_ImportUserDict(System.getProperty("user.dir")+"\\source\\userDic.txt");  
            resultString = cLibrary.NLPIR_ParagraphProcess(inputString, 1);  
            System.out.println("\n导入用户词典文件后分词结果为：\n" + resultString); 
            
            //提取的关键词则以#号分隔
            //NLPIR_GetKeyWords():从输入的段落中提取关键词
            resultString = cLibrary.NLPIR_GetKeyWords(inputString,10,false);  
            System.out.println("\n从段落中提取的关键词：\n" + resultString);
            
            //因输入的文字没有新词，均能被识别，所以新词提取结果为空
            //NLPIR_GetNewWords():从段落中提取新词
            resultString = cLibrary.NLPIR_GetNewWords(inputString, 10, false);  
            System.out.println("\n新词提取结果为：\n" + resultString);
            
            //NLPIR_FileProcess():处理一个TXT文件
            Double time = cLibrary.NLPIR_FileProcess(System.getProperty("user.dir") + "\\source\\inputFile.txt", 
            		System.getProperty("user.dir") + "\\source\\outputFile.txt", 1);
            if(time.isInfinite()) {
            	System.out.println("没有结果");
            } else {
            	BigDecimal bigDecimalTime = new BigDecimal(time);
            	System.out.println(bigDecimalTime.divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP) + "秒");
            }
            
            //NLPIR_GetFileKeyWords():从TXT文件中提取关键词
            resultString = cLibrary.NLPIR_GetFileKeyWords(System.getProperty("user.dir") + "\\source\\keyWordsFile.txt", 10, false);
            System.out.println("\n从文件中提取关键词的结果为：\n" + resultString);
            
            //NLPIR_Exit():退出程序并释放所有资源和NLPIR使用的缓存数据
            cLibrary.NLPIR_Exit();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("错误信息：");
			e.printStackTrace();
		}
	}

}
