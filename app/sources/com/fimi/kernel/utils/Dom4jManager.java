package com.fimi.kernel.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jManager {
    public <T> List<T> readXML(String xmlPath, String elementName, Class cls) {
        long lasting = System.currentTimeMillis();
        List<T> list = new ArrayList();
        try {
            Element root = new SAXReader().read(new File(xmlPath)).getRootElement();
            Field[] properties = cls.getDeclaredFields();
            Iterator i = root.elementIterator(elementName);
            while (i.hasNext()) {
                Element foo = (Element) i.next();
                T t = cls.newInstance();
                int j = 0;
                while (j < properties.length) {
                    if (!(properties[j].isSynthetic() || properties[j].getName().equals("serialVersionUID"))) {
                        t.getClass().getMethod("set" + properties[j].getName().substring(0, 1).toUpperCase() + properties[j].getName().substring(1), new Class[]{properties[j].getType()}).invoke(t, new Object[]{foo.attributeValue(properties[j].getName())});
                    }
                    j++;
                }
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("读取XML文件结束,用时" + (System.currentTimeMillis() - lasting) + "ms" + list.size());
        return list;
    }
}
