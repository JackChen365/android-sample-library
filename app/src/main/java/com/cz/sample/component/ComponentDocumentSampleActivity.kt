package com.cz.sample.component

import android.os.Bundle
import com.cz.android.sample.api.RefCategory
import com.cz.android.sample.api.RefRegister
import com.cz.android.sample.library.appcompat.SampleAppCompatActivity
import com.cz.android.sample.library.component.document.SampleDocument
import com.cz.sample.R

/**
 * This sample shows how to related to your sample document
 * Here are three different cases:
 *
 * <pre>
 * 1. @SampleDocument("DocumentSample.md") It will plus your repository path and your class's package
 * For example:
 * If your setup your repository url below:
 * @ProjectRepository("https://raw.githubusercontent.com/momodae/AndroidSampleLibrary/master/app/src/main/java/")
 * and your class package was:com.cz.sample.component
 * As a result your document's url will be:https://raw.githubusercontent.com/momodae/AndroidSampleLibrary/master/app/src/main/java/com/cz/sample/component/xxx.md
 *
 * Tips:
 *  Make sure when you open this sample your code was committed to the repository
 *
 * 2. @SampleDocument("http://xxx.DocumentSample.md") It will use this url to load the document
 *
 * 3. @SampleDocument("assets://DocumentSample.md") It will check the file from assets
 * </pre>
 *
 * @see SampleDocument This annotation will add a additional panel to your sample.
 */
@SampleDocument("documentSample.md")
@RefCategory(title=R.string.component_category,desc = R.string.component_category_desc,priority = 1)
@RefRegister(title=R.string.component_sample2,desc=R.string.component_sample2_desc,category = R.string.component_category,priority = 1)
class ComponentDocumentSampleActivity : SampleAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_document_sample)
    }
}
