package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        // Enum 안에 있는 BOOK, FOOD, ETC 를 배열로 넘겨준다
        // ItemType[] values = ItemType.values();
        return ItemType.values(); // Ctrl+Alt+N : Inline Variable (합치기)
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // @ModelAttribute("regions") 사용으로 아래 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>(); // HashMap은 순서 보장 X
//        regions.put("SEOUL", "서울"); // "SEOUL"은 서버에 왔다갔다 할 키값, "서울"은 사용자가 볼 값
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // th:object 를 적용하려면 먼저 해당 오브젝트 정보를 넘겨주어야 한다.
        // 등록 폼이기 때문에 데이터가 비어있는 빈 오브젝트를 만들어서 뷰에 전달.
        // addForm.html 에서 th:object, th:field 를 사용하기 위해 빈 객체 생성 (new Item())
        model.addAttribute("item", new Item());

        // @ModelAttribute("regions") 사용으로 아래 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/addForm";
    }

    // [ 체크 박스를 선택한 경우 ]
    // HTML Form 에서 open=on 이라는 값이 넘어간다. 스프링은 on 이라는 문자를 true 타입으로 변환 ==> item.open=true

    // [ 체크 박스를 선택하지 않은 경우 - 히든필드 사용 X ]
    // HTML Form 에서 open 이라는 필드 자체가 서버로 전송되지 않는다 ==> item.open=null

    // [ 체크 박스를 선택하지 않은 경우 - 히든필드 사용 O ]
    // HTML Form 에서 open 은 전송되지 않고 _open 만 전송 되는데, 이 경우 체크를 해제 했다고 판단 ==> item.open=false
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        // 상품 등록 폼에서 값이 잘 넘어 왔는지 확인
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        // @ModelAttribute("regions") 사용으로 아래 생략 가능
//        Map<String, String> regions = new LinkedHashMap<>();
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {

        log.info("item.open={}", item.getOpen());
        log.info("item={}", item);

        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }
}

