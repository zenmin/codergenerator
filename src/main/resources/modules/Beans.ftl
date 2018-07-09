<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

	<bean id="${lower}" class="com.hw.domain.${title}"/>
	<bean id="${lower}Dao" class="com.hw.dao.BaseDAO">
		<property name="dataConfig" value="tbl:manager.${lower}.de_${lower}"/>
		<property name="queryConfig" value="tbl.manager.${lower}.conf_${lower}"/>
		<property name="domain">
			<ref bean="${lower}"/>
		</property>
	</bean>
</beans>