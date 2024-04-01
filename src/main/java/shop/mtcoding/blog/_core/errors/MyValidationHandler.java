package shop.mtcoding.blog._core.errors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog._core.errors.exception.Exception400;

@Aspect // AOP 등록
@Component // IoC 등록
public class MyValidationHandler {

    // Advice (부가 로직 hello 메서드)
    // Advice 가 수행될 위치 == PointCut
    // 유효성 검사는 Post 일 때만 작동 하면 된다 (Post 일 때만 body 데이터가 존재하니깐!, 이걸 모르면 http에 대해서 잘 모르는 것!)
    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)") // 포인트 컷 [PostMapping, PutMapping 일때만 실행해라]
    public void hello(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs(); // 파라미터 (매개변수)
        System.out.println("크기 : " + args.length);

        for (Object arg : args){
            if (arg instanceof Errors){
                Errors errors = (Errors) arg;
                //부가로직을 여기로 빼서 한번에 코드 간편화
                if(errors.hasErrors()){
                    for (FieldError error : errors.getFieldErrors()){
                        System.out.println(error.getField());
                        System.out.println(error.getDefaultMessage());

                        throw new Exception400(error.getDefaultMessage()+" : "+error.getField());
                    }
                }
            }
        }

        System.out.println("MyValidationHandler : hello___________________");
    }
}
