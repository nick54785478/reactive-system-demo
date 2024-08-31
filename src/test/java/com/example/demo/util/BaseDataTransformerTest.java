package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BaseDataTransformerTest {

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InData {
		private Long id;
		private String num;
		private Date createDate;
		private String updateDate;
		private String bigDecimal1;
		private BigDecimal bigDecimal2;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OutData {
		private String id;
		private Long num;
		private String createDate;
		private Date updateDate;
		private BigDecimal bigDecimal1;
		private String bigDecimal2;
	}

	@Test
	void testDateToString() {
		InData inData = new InData();
		inData.setCreateDate(new Date());
		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getCreateDate());

		assertNotNull(outData.getCreateDate());
	}

	@Test
	void testStringToDate() {
		InData inData = new InData();
		inData.setUpdateDate("2024-09-01 00:00:00");

		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getUpdateDate());

		assertNotNull(outData.getUpdateDate());
	}

	@Test
	void testLongToString() {
		InData inData = new InData();
		inData.setId(1L);
		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getId());
	}

	@Test
	void testStringToLong() {
		InData inData = new InData();
		inData.setNum("1");
		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getNum());
	}

	@Test
	void testStringToBigDecimal() {
		InData inData = new InData();
		inData.setBigDecimal1("1");
		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getBigDecimal1());
	}

	@Test
	void testBigDecimalToString() {
		InData inData = new InData();
		inData.setBigDecimal2(new BigDecimal("1"));
		OutData outData = BaseDataTransformer.transformData(inData, OutData.class);
		log.info("Date: {}", outData.getBigDecimal2());
	}

	@Test
	void testBean1ListToBean2List() {
		InData inData1 = new InData();
		inData1.setId(1L);
		inData1.setNum("1");
		inData1.setCreateDate(new Date());
		inData1.setUpdateDate("2024-09-01 00:00:00");
		inData1.setBigDecimal1("1");
		inData1.setBigDecimal2(new BigDecimal("1"));
		InData inData2 = new InData();
		inData2.setId(2L);
		inData2.setNum("2");
		inData2.setCreateDate(new Date());
		inData2.setUpdateDate("2024-09-01 00:00:00");
		inData2.setBigDecimal1("2");
		inData2.setBigDecimal2(new BigDecimal("2"));
		List<InData> inDataList = List.of(inData1, inData2);
		List<OutData> outData = BaseDataTransformer.transformData(inDataList, OutData.class);
		log.info("Out Data List: {}", outData);
	}
}
