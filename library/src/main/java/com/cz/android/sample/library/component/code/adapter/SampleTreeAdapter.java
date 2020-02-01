package com.cz.android.sample.library.component.code.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cz.android.sample.library.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cz on 16/1/23.
 * 一个RecyclerView的树形管理Adapter对象
 * 2级  父级,子级
 * 多级  父级,根->
 *
 * root
 *
 */
public abstract class SampleTreeAdapter<E> extends BaseAdapter {
    protected final ArrayList<TreeNode<E>> nodeItems;
    protected final ArrayList<E> items;
    protected final TreeNode<E> rootNode;
    private final int levelPadding;
    private OnNodeItemClickListener listener;


    public SampleTreeAdapter(Context context,TreeNode<E> rootNode) {
        this.levelPadding= (int) context.getResources().getDimension(R.dimen.sample_tree_level_padding);
        this.rootNode = rootNode;
        this.nodeItems = new ArrayList<>();
        this.items = new ArrayList<>();
        refreshItems();
    }

    /**
     * 获取节点内所有可展开节点
     * 这里效率稍微了点,但可以接受
     */
    private synchronized ArrayList<TreeNode<E>> getNodeItems(TreeNode rootNode) {
        ArrayList<TreeNode<E>> nodeItems = new ArrayList<>();
        LinkedList<TreeNode<E>> nodes = new LinkedList<>();
        nodes.add(rootNode);
        while (!nodes.isEmpty()) {
            TreeNode<E> node = nodes.pollFirst();
            if (this.rootNode == node || node.isExpand && !node.children.isEmpty()) {
                ArrayList<TreeNode<E>> child = node.children;
                int size = child.size();
                for (int i = size - 1; i >= 0; i--) {
                    TreeNode<E> childNode = child.get(i);
                    nodes.offerFirst(childNode);
                }
            }
            if (node != rootNode) {
                nodeItems.add(node);
            }
        }
        return nodeItems;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public abstract View onCreateView(ViewGroup viewGroup,int itemViewType,int position);

    public abstract void onBindView(final View view, TreeNode<E> node, E e, int viewType, int position);

    protected int getLevelPadding(){
        return levelPadding;
    }

    private void offsetNodeView(View view, TreeNode<E> node){
        int levelPadding = getLevelPadding();
        int paddingTop = view.getPaddingTop();
        int paddingBottom = view.getPaddingBottom();
        int paddingRight = view.getPaddingRight();
        view.setPadding(levelPadding * node.level,paddingTop,paddingRight,paddingBottom);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        TreeNode<E> node = getNode(position);
        int itemViewType = getItemViewType(position);
        if(null==view){
            view=onCreateView(viewGroup,itemViewType,position);
        }
        offsetNodeView(view,node);
        onBindView(view, node, node.e, itemViewType, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode<E> node = getNode(position);
                boolean expand = node.isExpand;
                node.isExpand = true;//置为true,取得当前展开后的节点
                ArrayList<E> items = getItems(node);
                final ArrayList<TreeNode<E>> addNodes = getNodeItems(node);
                node.isExpand = !expand;//更新展开状态
                if (!addNodes.isEmpty()) {
                    onNodeExpand(node, v, !expand);
                    if (expand) {
                        SampleTreeAdapter.this.items.removeAll(items);
                        nodeItems.removeAll(addNodes);
                        notifyDataSetChanged();
                    } else {
                        SampleTreeAdapter.this.items.addAll(position + 1, items);
                        nodeItems.addAll(position + 1, addNodes);
                        notifyDataSetChanged();
                    }
                } else if (null != listener) {
                    listener.onNodeItemClick(node, v, position);
                }
            }
        });
        return view;
    }



    /**
     * 子类实现,节点展开或关闭
     *
     * @param node
     * @param v
     * @param expand
     */
    protected void onNodeExpand(TreeNode<E> node, View v, boolean expand) {
    }

    /**
     * 获得列表对应位置节点
     *
     * @param position
     * @return
     */
    public TreeNode<E> getNode(int position) {
        return nodeItems.get(position);
    }

    /**
     * 获得对应节点内容
     *
     * @param position
     * @return
     */
    public E getItem(int position) {
        return nodeItems.get(position).e;
    }

    public List<E> getItems(){
        return items;
    }

    @Override
    public int getCount() {
        return nodeItems.size();
    }

    public void setOnNodeItemClickListener(OnNodeItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 获取节点内所有可展开节点
     * 这里效率稍微了点,但可以接受
     */
    private ArrayList<E> getItems(TreeNode rootNode) {
        ArrayList<E> nodeItems = new ArrayList<>();
        LinkedList<TreeNode<E>> nodes = new LinkedList<>();
        nodes.add(rootNode);
        while (!nodes.isEmpty()) {
            TreeNode<E> node = nodes.pollFirst();
            if (this.rootNode == node || node.isExpand && !node.children.isEmpty()) {
                ArrayList<TreeNode<E>> child = node.children;
                int size = child.size();
                for (int i = size - 1; i >= 0; i--) {
                    TreeNode<E> childNode = child.get(i);
                    nodes.offerFirst(childNode);
                }
            }
            if (node != rootNode) {
                nodeItems.add(node.e);
            }
        }
        return nodeItems;
    }

    private ArrayList<E> getItems(List<TreeNode<E>> nodes) {
        ArrayList<E> items = new ArrayList<>();
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            items.add(nodes.get(i).e);
        }
        return items;
    }

    /**
     * 移除指定节点
     *
     * @param node
     */
    public void removeNode(TreeNode<E> node) {
        if (null != node) {
            ArrayList<TreeNode<E>> childNodes = node.children;
            //移除节点内,所有子节点
            if (node.isExpand && !childNodes.isEmpty()) {
                int size = childNodes.size();
                //这里之所以反向减少.是因为正向减少的话.这边减,在递归里,child的条目在减少.正向会引起size,没减,但child减少的角标越界问题.反向则不会
                for (int i = size - 1; i >= 0; i--) {
                    TreeNode<E> treeNode = childNodes.get(i);
                    removeNode(treeNode);
                }
            }
            int index = nodeItems.indexOf(node);
            if (0 <= index) {
                remove(index);
            }
        }
    }

    public void removeNode(int position) {
        removeNode(nodeItems.get(position));
    }

    /**
     * 获取条目在节点位置
     *
     * @param e
     * @return
     */
    public int indexOfItem(E e) {
        return items.indexOf(e);
    }

    /**
     * 设置指定条目取值
     *
     * @param index
     * @param e
     */
    public void set(int index, E e) {
        items.set(index, e);
        TreeNode<E> node = nodeItems.get(index);
        node.e = e;
        notifyDataSetChanged();
    }

    /**
     * 按位置移除
     *
     * @param position
     */
    private void remove(int position) {
        items.remove(position);
        TreeNode<E> node = nodeItems.remove(position);
        //移除根节点内节点指向
        TreeNode<E> parent = node.parent;
        if (null != parent) {
            parent.children.remove(node);
        }
        notifyDataSetChanged();
    }

    public void insertNode(E e) {
        insertNode(new TreeNode(rootNode, e));
    }

    /**
     * 插入节点
     *
     * @param node
     */
    public void insertNode(TreeNode<E> node) {
        node.parent = rootNode;
        rootNode.children.add(node);
        ArrayList<TreeNode<E>> nodeItems = new ArrayList<>();
        nodeItems.add(node);
        ArrayList<TreeNode<E>> items = getNodeItems(node);
        if (!items.isEmpty()) {
            nodeItems.addAll(items);
        }
        int itemCount = getCount();
        this.nodeItems.addAll(nodeItems);
        this.items.addAll(getItems(nodeItems));
        notifyDataSetChanged();
    }

    public void refreshItems(){
        this.items.clear();
        this.nodeItems.clear();
        ArrayList<TreeNode<E>> nodes = getNodeItems(this.rootNode);
        if (null != nodes) {
            this.items.addAll(getItems(nodes));
            this.nodeItems.addAll(nodes);
        }
    }

    /**
     * 树节点
     *
     * @param <E>
     */
    public static class TreeNode<E> {
        public boolean isExpand;//是否展开
        public E e;//节点
        public int level;//当前节点级 0 1 2
        public TreeNode<E> parent;//父节点
        public ArrayList<TreeNode<E>> children;//子节点

        public TreeNode(E e) {
            this(false, null, e);
        }

        public TreeNode(TreeNode<E> parent, E e) {
            this(false, parent, e);
        }

        public TreeNode(boolean isExpand, TreeNode<E> parent, E e) {
            this.isExpand = isExpand;
            this.e = e;
            this.level = null == parent ? 0 : parent.level + 1;
            this.children = new ArrayList<>();
            this.parent = parent;
            if(null!=parent){
                this.parent.children.add(this);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            boolean result = false;
            TreeNode r = (TreeNode) o;
            if (null != e && null != r.e) {
                result = e.equals(r.e);
            }
            return result;
        }

        @Override
        public String toString() {
            return e.toString();
        }

    }
    /**
     * Created by cz on 16/3/17.
     */
    public interface OnNodeItemClickListener<E> {
        void onNodeItemClick(SampleTreeAdapter.TreeNode<E> node, View v, int position);
    }
}