package com.cz.sample.test

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cz.android.sample.library.component.code.SampleSourceCode
import com.cz.android.sample.library.component.code.adapter.SampleTreeAdapter
import com.cz.sample.R
import com.cz.sample.test.adapter.SampleFileAdapter
import kotlinx.android.synthetic.main.fragment_sample_tree.*
import java.io.File
import kotlin.concurrent.thread

/**
 * This sample demonstrated how to output message and show it to your sample
 * @see SampleSourceCode add additional panel that show all the source code
 */
//@SamplePermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//@RefRegister(title=R.string.other_sample4,desc = R.string.other_sample4_desc,category = R.string.other)
class SampleTreeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_tree, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val progressDialog=ProgressDialog(context)
        progressDialog.setMessage(getString(R.string.loading))
        progressDialog.setCancelable(false)
        progressDialog.show()
        thread {
            val file = Environment.getExternalStorageDirectory()
            val rootNode = SampleTreeAdapter.TreeNode<File>(file)
            if(null!=file.listFiles()){
                for(f in file.listFiles()){
                    fileTreeTraversal(f,rootNode)
                }
            }
            view?.post {
                progressDialog.dismiss()
                fileTreeList.adapter=SampleFileAdapter(context,rootNode)
            }
        }
    }

    /**
     * File traversal and build file tree node
     */
    private fun fileTreeTraversal(file: File, parentNode: SampleTreeAdapter.TreeNode<File>){
        val subTreeNode = SampleTreeAdapter.TreeNode(parentNode,file)
        if(file.isDirectory){
            for(f in file.listFiles()){
                fileTreeTraversal(f,subTreeNode)
            }
        }
    }
}