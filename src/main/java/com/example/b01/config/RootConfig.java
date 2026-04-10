package com.example.b01.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// bean 설정에 대한 클래스다라는 것을 보여줌
public class RootConfig {
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                //필드 매칭 활성화: 메서드(Getter/Setter)가 아니라 실제 필드 이름을 직접 비교함
                //Getter Setter대신 필드값만 비교
                .setFieldMatchingEnabled(true)
                //접근 제한자 허용 범위: 필드가 private이라도 접근해서 값을 읽고 쓸 수 있게 설정
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)

                // 매핑 전략 설정: 매칭 강도를 'LOOSE(느슨하게)'로 설정
                //중첩 객체(계층 구조)에서 마지막 속성의 토큰이 하나라도 매칭되면 허용하는 전략
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
