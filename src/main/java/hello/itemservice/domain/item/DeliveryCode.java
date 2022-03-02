package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 배송 방식은 DeliveryCode 라는 클래스를 사용한다.
 * FAST   : 빠른 배송
 * NORMAL : 일반 배송
 * SLOW   : 느린 배송
 */
@Data
@AllArgsConstructor
public class DeliveryCode {

    private String code;        // 'FAST' 같은 시스템에서 전달하는 값
    private String displayName; // '빠른배송' 같은 고객에게 보여주는 값
}
