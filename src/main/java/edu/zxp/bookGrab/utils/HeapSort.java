package edu.zxp.bookGrab.utils;

import java.util.Arrays;

import edu.zxp.bookGrab.entity.Book;

public class HeapSort {
	private Book[] arr;
	public HeapSort(Book[] arr){
        this.arr = arr;
    }
    
    /**
     * 堆排序的主要入口方法，共两步。
     */
    public void sort(){
        /*
         *  第一步：将数组堆化
         *  beginIndex = 第一个非叶子节点。
         *  从第一个非叶子节点开始即可。无需从最后一个叶子节点开始。
         *  叶子节点可以看作已符合堆要求的节点，根节点就是它自己且自己以下值为最大。
         */
        int len = arr.length - 1;
        int beginIndex = (len - 1) >> 1; 
        for(int i = beginIndex; i >= 0; i--){
            maxHeapify(i, len);
        }
        
        /*
         * 第二步：对堆化数据排序
         * 每次都是移出最顶层的根节点A[0]，与最尾部节点位置调换，同时遍历长度 - 1。
         * 然后从新整理被换到根节点的末尾元素，使其符合堆的特性。
         * 直至未排序的堆长度为 0。
         */
        for(int i = len; i > 0; i--){
            swap(0, i);
            maxHeapify(0, i - 1);
        }
    }
    
    private void swap(int i,int j){
    	Book temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * 调整索引为 index 处的数据，使其符合堆的特性。
     * 
     * @param index 需要堆化处理的数据的索引
     * @param len 未排序的堆（数组）的长度
     */
    private void maxHeapify(int index,int len){
        int li = (index << 1) + 1; // 左子节点索引
        int ri = li + 1;           // 右子节点索引
        //int cMax = li;             // 子节点值最大索引，默认左子节点。
        int cMin = li ;
        if(li > len) return;       // 左子节点索引超出计算范围，直接返回。
        if(ri <= len && (null == arr[ri] ? 0f :arr[ri].getScore()) < (null == arr[li] ? 0f : arr[li].getScore())) // 先判断左右子节点，哪个较大。
        	cMin = ri;
        if((null == arr[cMin] ? 0f : arr[cMin].getScore()) < (null == arr[index] ? 0f : arr[index].getScore())){
            swap(cMin, index);      // 如果父节点被子节点调换，
            maxHeapify(cMin, len);  // 则需要继续判断换下后的父节点是否符合堆的特性。
        }
    }
    
    static void  HeapInsert(Book[] heap, int n, Book book)
    {
        int i, j;
     
        heap[n] = book;//num插入堆尾
        i = n;
        j = (n - 1) / 2;//j指向i的父结点
         
        //注意不要漏掉i!=0的条件。因为必须保证i有父结点j。j>=0并不能保证i!=0。
        //如果没有此条件，当i=0时，j=0,若heap[0]>num,程序就会陷入死循环。
        while (j >= 0 && i != 0)
        {
            if (heap[j].getScore() >= book.getScore())
                break;
            heap[i] = heap[j];
            i = j;
            j = (i - 1) / 2;
        }
        heap[i] = book;    
    }
    

}
