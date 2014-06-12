/**
 * Copyright 1996-2014 FoxBPM ORG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author MAENLIANG
 */
package org.foxbpm.engine.impl.svg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.foxbpm.engine.exception.FoxBPMException;
import org.foxbpm.engine.exception.FoxBPMExpressionException;
import org.foxbpm.engine.impl.svg.vo.SvgVO;
import org.foxbpm.engine.impl.svg.vo.VONode;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * SVG模版资源维护的单例对象
 * 
 * @author MAENLIANG
 * @date 2014-06-08
 * 
 */
public class SVGTemplateContainer {

	private static final String BPMN_PATH = "target/classes/bpmn2.0/view";
	private static SVGTemplateContainer container = new SVGTemplateContainer();
	private Map<String, VONode> svgTemplets = new HashMap<String, VONode>();

	private SVGTemplateContainer() {
		svgTemplets = new HashMap<String, VONode>();
	}

	/**
	 * 第一次需要从svg文档加载
	 * 
	 * @param templateName
	 */
	public void init(String templateName) {
		File svgFile = new File(BPMN_PATH + File.separator + templateName);
		if (svgFile == null) {
			throw new FoxBPMException("template svg file not exists");
		}
		BufferedReader bufferReader = null;
		try {

			bufferReader = new BufferedReader(new FileReader(svgFile));
			String tempLineStr = "";
			StringBuffer svgStrBuffer = new StringBuffer(tempLineStr);
			while ((tempLineStr = bufferReader.readLine()) != null) {
				svgStrBuffer.append(tempLineStr);
			}
			StringReader stringReader = new StringReader(
					svgStrBuffer.toString());

			JAXBContext context = JAXBContext.newInstance(SvgVO.class);
			Unmarshaller unMarshaller = context.createUnmarshaller();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 解析的时候忽略SVG命名空间，否则会出错
			factory.setNamespaceAware(true);
			XMLReader reader = factory.newSAXParser().getXMLReader();
			Source source = new SAXSource(reader, new InputSource(stringReader));
			VONode object = (VONode) unMarshaller.unmarshal(source);
			container.svgTemplets.put(templateName, object);
		} catch (Exception e) {
			throw new FoxBPMException("template svg file load exception", e);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * 获取模版所在容器的实例
	 * 
	 * @return
	 */
	public static SVGTemplateContainer getContainerInstance() {
		return container;
	}

	/**
	 * 获取svg模版
	 * 
	 * @param templateName
	 * @return
	 */
	public VONode getTemplateByName(String templateName) {
		// 第一次需要从svg文档初始化
		if (container.svgTemplets.get(templateName) == null) {
			container.init(templateName);
		}
		return container.svgTemplets.get(templateName);
	}
}
