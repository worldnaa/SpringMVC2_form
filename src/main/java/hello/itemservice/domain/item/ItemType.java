package hello.itemservice.domain.item;

// 상품 종류는 ENUM 을 사용한다. 설명을 위해 description 필드를 추가했다.
public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    // html에서 프로퍼티 접근법 사용 시 필요 (type.description)
    public String getDescription() {
        return description;
    }
}


