package com.msymobile.www.commons.dao;

public interface BaseMapper<T> {

	int deleteByPrimaryKey(String id);
	
	int deleteByPrimaryKey(Integer id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(String id);
    
    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);
}
