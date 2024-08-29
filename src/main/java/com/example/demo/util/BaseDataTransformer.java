package com.example.demo.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseDataTransformer {

	protected static final ModelMapper modelMapper = new ModelMapper();

	/**
	 * 資料轉換
	 * 
	 * @param target 目標物件
	 * @param clazz  欲轉換的型別
	 * @return 轉換後的物件
	 */
	public static <T> T transformData(Object target, Class<T> clazz) {
		return modelMapper.map(target, clazz);
	}
}
