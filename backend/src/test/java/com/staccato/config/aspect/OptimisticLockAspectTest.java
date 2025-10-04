package com.staccato.config.aspect;

import com.staccato.config.aspect.annotation.OptimisticLockHandler;
import com.staccato.exception.ConflictException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OptimisticLockAspectTest {

    private final OptimisticLockAspect aspect = new OptimisticLockAspect();

    static class Target {
        @Transactional
        @OptimisticLockHandler("이미 처리 완료된 초대입니다.")
        public void annotated() {}

        @Transactional
        public void nonAnnotated() {}
    }

    @DisplayName("OptimisticLock 예외 발생 시, @OptimisticLockHandler 메시지로 ConflictException이 래핑된다.")
    @Test
    void wrapsOptimisticLockWithCustomMessage() throws Exception {
        // given
        ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Method method = Target.class.getDeclaredMethod("annotated");

        when(pjp.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(method);
        when(pjp.proceed()).thenThrow(new OptimisticLockingFailureException("conflict"));

        // when & then
        assertThatThrownBy(() -> aspect.wrapOptimisticLock(pjp))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 처리 완료된 초대입니다.");
    }

    @DisplayName("OptimisticLock 예외 발생 시, 기본 메시지로 ConflictException이 래핑된다.")
    @Test
    void wrapsOptimisticLockWithDefaultMessage() throws Exception {
        // given
        ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Method method = Target.class.getDeclaredMethod("nonAnnotated");

        when(pjp.getSignature()).thenReturn(signature);
        when(signature.getMethod()).thenReturn(method);
        when(pjp.proceed()).thenThrow(new OptimisticLockingFailureException("conflict"));

        // when & then
        String defaultMessage = new ConflictException().getMessage();
        assertThatThrownBy(() -> aspect.wrapOptimisticLock(pjp))
                .isInstanceOf(ConflictException.class)
                .hasMessage(defaultMessage);
    }
}