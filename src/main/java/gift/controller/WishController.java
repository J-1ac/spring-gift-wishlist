package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.ProductDto;
import gift.domain.model.User;
import gift.domain.model.WishResponseDto;
import gift.service.WishService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WishResponseDto> getWishes(@LoginUser User user) {
        return wishService.getProductsByUserEmail(user);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> addWish(@PathVariable Long productId,
        @LoginUser User user) {
        ProductDto wishedProduct = wishService.addWish(user.getEmail(), productId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "상품이 성공적으로 추가되었습니다.");
        response.put("data", wishedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId, @LoginUser User user) {
        wishService.deleteProduct(user.getEmail(), productId);
    }
}
