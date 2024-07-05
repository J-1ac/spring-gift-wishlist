package gift.domain.repository;

import gift.domain.model.Product;
import gift.domain.model.WishResponseDto;
import java.util.Collections;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WishRepository {

    private final JdbcTemplate jdbcTemplate;

    public WishRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WishResponseDto> getProductsByUserEmail(String email) {
        String sql = "SELECT w.id, p.id as product_id, p.name as product_name, " +
            "p.price as product_price, p.imageurl as product_image_url " +
            "FROM wishlists w " +
            "JOIN products p ON w.product_id = p.id " +
            "WHERE w.user_email = ?";

        return jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) ->
            new WishResponseDto(
                rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getLong("product_price"),
                rs.getString("product_image_url")
            )
        );
    }

    public void addWish(String email, Long productId) {
        jdbcTemplate.update(
            "INSERT INTO wishlists (user_email, product_id) VALUES (?, ?)",
            email,
            productId
        );
    }

    public boolean isExistWish(String email, Long productId) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
            "SELECT EXISTS(SELECT 1 FROM wishlists WHERE user_email = ? AND product_id = ?)",
            Boolean.class,
            email,
            productId
        ));
    }

    public void deleteWish(String email, Long productId) {
        jdbcTemplate.update(
            "DELETE FROM wishlists WHERE user_email = ? AND product_id = ?",
            email,
            productId
        );
    }
}
