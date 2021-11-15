<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 日志的输出目录 -->
    <property name="log.path" value="D:/360MoveData/Users/Lenovo/Desktop/上课及复习代码/idea/srbProject/srb/log" />

    <!--控制台日志格式：彩色日志-->
    <!-- magenta:洋红 -->
    <!-- boldMagenta:粗红-->
    <!-- cyan:青色 -->
    <!-- white:白色 -->
    <!-- magenta:洋红 -->
    <property name="CONSOLE_LOG_PATTERN"
              value="%green(%date{yyyy-MM-dd HH:mm:ss}) %highlight([%-5level]) %yellow(%logger) %msg%n"/>

    <!--文件日志格式-->
    <property name="FILE_LOG_PATTERN"
              value="%date{yyyy-MM-dd HH:mm:ss} [%-5level] %thread %file:%line %logger %msg%n" />

    <!--编码-->
    <property name="ENCODING"
              value="UTF-8" />

    <!-- 控制台日志 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
            <charset>${ENCODING}</charset>
        </encoder>
    </appender>

    <!-- 文件日志 -->
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>${log.path}/log.log</file>
        <append>true</append>
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset>${ENCODING}</charset>
        </encoder>
    </appender>

<!--    &lt;!&ndash; 日志记录器  &ndash;&gt;-->
<!--    <logger name="com.acho" level="INFO">-->
<!--        <appender-ref ref="CONSOLE" />-->
<!--        <appender-ref ref="FILE" />-->
<!--    </logger>-->

        <!--  滚动策略  -->
    <appender name="ROLLING_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">

        <!--  要区别于其他的appender中的文件名字  -->
        <file>${log.path}/log-rolling.log</file>
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset>${ENCODING}</charset>
        </encoder>


        <!-- 设置滚动日志记录的滚动策略 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 日志归档路径以及格式 -->
            <fileNamePattern>${log.path}/info/log-rolling-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <!--归档日志文件保留的最大数量-->
            <maxHistory>15</maxHistory>

            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>1KB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>

    </appender>


<!--  多环境配置  -->
    <!-- 开发环境和测试环境 -->
    <springProfile name="dev,test">
        <logger name="com.acho" level="INFO">
            <appender-ref ref="CONSOLE" />
        </logger>
    </springProfile>

    <!-- 生产环境 -->
    <springProfile name="prod">
        <logger name="com.acho" level="ERROR">
            <appender-ref ref="CONSOLE" />
            <appender-ref ref="ROLLING_FILE" />
        </logger>
    </springProfile>
</configuration>