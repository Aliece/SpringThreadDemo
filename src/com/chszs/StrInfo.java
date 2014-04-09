package com.chszs;

/**
 * @title: StrInfo.java
 * @description: 
 * @copyright:
 * @company: 
 * @author saizhongzhang
 * @date 2014年4月3日
 * @version 1.0
 */

public class StrInfo implements Comparable<StrInfo> {
	private int count;  
    private String string;  

    public StrInfo(int count, String string) {  
        super();  
        this.count = count;  
        this.string = string;  
    }  

    public int getCount() {  
        return count;  
    }  

    public void setCount(int count) {  
        this.count = count;  
    }  


    public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override  
    public int compareTo(StrInfo o) {  
        if (this.count > o.getCount()) {  
            return 1;  
        } else if (this.count < o.getCount()) {  
            return -1;  
        } else {  
            return 0;  
        }  
    }  

    public String toString() {  
        return this.getString() + " -- " + this.count;  
    }  

}
