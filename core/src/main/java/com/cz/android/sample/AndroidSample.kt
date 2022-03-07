package com.cz.android.sample

import android.app.Application
import android.content.Context
import com.cz.android.sample.main.SampleActivityLifeCycleCallback
import com.cz.android.sample.function.FunctionManager
import com.cz.android.sample.api.SampleItem
import androidx.appcompat.app.AppCompatActivity
import com.cz.android.sample.api.ExtensionItem
import com.cz.android.sample.component.ComponentManager
import com.cz.android.sample.extension.ExtensionHandler
import com.cz.android.sample.processor.ActionProcessManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * @author Created by cz
 * @date 2020-01-31 10:03
 * @email bingo110@126.com
 */
abstract class AndroidSample protected constructor() {
    companion object {
        val instance: AndroidSample = AndroidSampleImpl()
    }

    private var applicationContext: Context? = null
    open fun attachToContext(context: Context) {
        val application = context.applicationContext as Application
        application.registerActivityLifecycleCallbacks(SampleActivityLifeCycleCallback())
        applicationContext = application
    }

    abstract fun getTestCases(): List<SampleItem>

    abstract fun <E> getExtension(clazz: Class<E>): E

    abstract fun getFunctionManager(): FunctionManager

    abstract fun getComponentManager(): ComponentManager

    abstract fun getPathNode(): PathNode

    /**
     * Start a sample.
     *
     * @param context
     * @param item
     */
    abstract fun start(context: AppCompatActivity?, item: SampleItem?)

    class AndroidSampleImpl : AndroidSample() {
        private val rootNode = PathNode(".")
        private val actionProcessor = ActionProcessManager()
        private val sampleItemList: MutableList<SampleItem> = ArrayList()
        private val extensionHandlers = mutableMapOf<Class<*>, ExtensionHandler<*>>()

        init {
            extensionHandlers[FunctionManager::class.java] = FunctionManager()
            extensionHandlers[ComponentManager::class.java] = ComponentManager.getInstance()
        }

        override fun attachToContext(context: Context) {
            super.attachToContext(context)
            val assets = context.assets
            try {
                val text = assets.open(SampleConstants.SAMPLE_CONFIGURATION_FILE_NAME).use { inputStream ->
                    val bytes = ByteArray(inputStream.available())
                    inputStream.read(bytes)
                    String(bytes)
                }
                val jsonObject = JSONObject(text)
                initialSampleItems(jsonObject)
                initialExtensions(jsonObject)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun initialSampleItems(jsonObject: JSONObject) {
            val sampleArray = jsonObject.getJSONArray("samples")
            val sampleList = deserializationSampleList(sampleArray)
            sampleItemList.addAll(sampleList)
            mergeSampleItems(sampleList)
        }

        private fun deserializationSampleList(sampleArray: JSONArray): List<SampleItem> {
            val sampleItemList = mutableListOf<SampleItem>()
            for (i in 0 until sampleArray.length()) {
                val sampleItem = SampleItem()
                val sampleObject = sampleArray.getJSONObject(i)
                sampleItem.title = sampleObject.getString("title")
                sampleItem.desc = sampleObject.getString("desc")
                sampleItem.path = sampleObject.getString("path")
                sampleItem.className = sampleObject.getString("className")
                sampleItem.isTestCase = sampleObject.getBoolean("isTestCase")
                sampleItemList.add(sampleItem)
            }
            return sampleItemList
        }

        private fun deserializationExtensionList(extensionsArray: JSONArray): List<ExtensionItem> {
            val extensionItemList = mutableListOf<ExtensionItem>()
            for (i in 0 until extensionsArray.length()) {
                val extensionObject = extensionsArray.getJSONObject(i)
                val extensionItem = ExtensionItem()
                extensionItem.className = extensionObject.getString("className")
                extensionItem.superClass = extensionObject.getString("superClass")
                val interfaceArray = extensionObject.getJSONArray("interfaces")
                extensionItem.interfaces = Array<String>(interfaceArray.length()) { index ->
                    interfaceArray.getString(index)
                }
                extensionItemList.add(extensionItem)
            }
            return extensionItemList
        }

        private fun initialExtensions(jsonObject: JSONObject) {
            val extensionArray = jsonObject.getJSONArray("extensions")
            val extensionList = deserializationExtensionList(extensionArray)
            extensionList.forEach { extensionItem ->
                extensionHandlers.values.forEach { extensionHandler ->
                    extensionHandler.handle(
                        extensionItem.className, extensionItem.superClass,
                        extensionItem.interfaces.toList()
                    )
                }
            }
        }

        private fun mergeSampleItems(sampleItemList: List<SampleItem>) {
            val root = rootNode
            val map = mutableMapOf<String, PathNode>()
            sampleItemList.forEach { sampleItem ->
                var prevNode = root
                val pathList = sampleItem.path.split("/")
                pathList.forEach { path ->
                    var pathNode = map[path]
                    if (null == pathNode) {
                        pathNode = PathNode(path)
                        map[path] = pathNode
                        prevNode.add(pathNode)
                    }
                    if (false == pathNode.parent?.contains(pathNode)) {
                        pathNode.parent?.add(pathNode)
                        prevNode.add(pathNode)
                    }
                    prevNode = pathNode
                }
            }
            //Collect the single node in the path.
            val singleNodeList = mutableListOf<PathNode>()
            collectSingleNode(singleNodeList, root, root.isSingle())
            //Make the single node as simple as possible.
            singleNodeList.forEach(PathNode::shiftUp)
        }

        private fun collectSingleNode(singleNodeList: MutableList<PathNode>, node: PathNode, singleNode: Boolean) {
            node.children.forEach { child ->
                collectSingleNode(singleNodeList, child, node.isSingle())
            }
            if (node.children.isEmpty() && singleNode) {
                singleNodeList.add(node)
            }
        }

        override fun getFunctionManager(): FunctionManager {
            return getExtension(FunctionManager::class.java)
        }

        override fun getComponentManager(): ComponentManager {
            return getExtension(ComponentManager::class.java)
        }

        override fun <E> getExtension(clazz: Class<E>): E {
            return extensionHandlers[clazz] as E
        }

        override fun getTestCases(): List<SampleItem> {
            val testcases: MutableList<SampleItem> = ArrayList()
            for (sampleItem in sampleItemList) {
                if (sampleItem.isTestCase) {
                    testcases.add(sampleItem)
                }
            }
            return testcases
        }

        override fun getPathNode() = rootNode

        override fun start(context: AppCompatActivity?, item: SampleItem?) {
            val functionManager = getFunctionManager()
            actionProcessor.process(functionManager, context, item)
        }
    }

    class PathNode(var item: Any? = null) {
        var parent: PathNode? = null
        var children: ArrayList<PathNode> = ArrayList(1)

        fun contains(pathNode: PathNode): Boolean {
            return children.contains(pathNode)
        }

        fun add(pathNode: PathNode) {
            children.add(pathNode)
            pathNode.parent = this
        }

        fun remove(node: PathNode?) {
            children.remove(node)
        }

        fun isEmpty(): Boolean {
            return children.isEmpty()
        }

        fun path(path: String): List<PathNode> {
            val pathList = path.split("/")
            var children = children
            pathList.forEach { path ->
                val child = children.find { it.item == path }
                if (null != child) {
                    children = child.children
                }
            }
            return children
        }

        fun shiftUp() {
            if (null == parent) return
            parent?.remove(this)
            val grandParent = parentOf(parent)
            grandParent?.children?.add(this)
            if (true == parent?.isEmpty()) {
                grandParent?.remove(parent)
            }
            parent = grandParent
            if (null != parentOf(parent) && true == parent?.isSingle()) {
                shiftUp()
            }
        }

        private fun parentOf(p: PathNode?): PathNode? {
            return p?.parent
        }

        fun isSingle(): Boolean {
            return 1 == children.size
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as PathNode
            if (item != other.item) return false
            return true
        }

        override fun hashCode(): Int {
            return item?.hashCode() ?: 0
        }

        override fun toString(): String {
            return item?.toString() ?: super.toString()
        }
    }
}