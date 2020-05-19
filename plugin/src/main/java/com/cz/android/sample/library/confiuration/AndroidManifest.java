package com.cz.android.sample.library.confiuration;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Created by cz
 * @date 2020-05-17 11:43
 * @email bingo110@126.com
 */
public class AndroidManifest {

    public AndroidManifest() {
    }

    /**
     * You could learn examples here.
     * https://memorynotfound.com/query-xml-xpath-jdom/
     * @throws JDOMException
     * @throws IOException
     */
    @Nullable
    public ManifestInformation parseManifestFile(File file){
        try {
            return parserAndroidManifest(file);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nonnull
    private ManifestInformation parserAndroidManifest(File manifestFile) throws JDOMException, IOException {
        ManifestInformation manifestInformation=new ManifestInformation();
        if(!manifestFile.exists()){
            System.out.println("File:"+manifestFile.getPath()+" not exists!");
        } else {
            SAXBuilder saxBuilder=new SAXBuilder();
            Document document = saxBuilder.build(manifestFile);
            XPathFactory xPathFactory = XPathFactory.instance();
            Namespace androidNameSpace = Namespace.getNamespace("android", "http://schemas.android.com/apk/res/android");
            XPathExpression<Attribute> applicationExpression = xPathFactory.compile("//application/@android:name", Filters.attribute(),null,androidNameSpace);
            Attribute applicationAttribute = applicationExpression.evaluateFirst(document);
            if(null!=applicationAttribute){
                manifestInformation.application=applicationAttribute.getValue();
            }
            XPathExpression<Attribute> applicationIdExpression = xPathFactory.compile("/manifest/@package", Filters.attribute(),null,androidNameSpace);
            Attribute applicationIdAttribute = applicationIdExpression.evaluateFirst(document);
            if(null!=applicationIdAttribute){
                manifestInformation.applicationId=applicationIdAttribute.getValue();
            }

            XPathExpression<Attribute> expression = xPathFactory.compile("//activity/@android:name", Filters.attribute(),null,androidNameSpace);
            List<Attribute> attributeList = expression.evaluate(document);
            for (int i = 0; i < attributeList.size(); i++) {
                Attribute attribute = attributeList.get(i);
                manifestInformation.addActivity(attribute.getValue());
            }
        }
        return manifestInformation;
    }

    public class ManifestInformation{
        public String application;
        public String applicationId;
        public List<String> activities=new ArrayList<>();

        public ManifestInformation() {
        }

        public void addActivity(String activity){
            activities.add(activity);
        }

        public boolean isApplicationClassFile(String classPath){
            return classPath.equals(application);
        }

        public boolean isActivityClassFile(String classPath){
            for(String activity:activities){
                if(activity.equals(classPath)){
                    return true;
                }
            }
            return false;
        }
    }
}
