package payroad.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payroad.domain.category.Category;
import payroad.domain.category.repository.CategoryRepository;
import payroad.global.response.exception.GeneralException;
import payroad.global.response.status.ErrorStatus;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
            .orElseThrow(() -> new GeneralException(ErrorStatus.CATEGORY_NOT_FIND));
    }

}
