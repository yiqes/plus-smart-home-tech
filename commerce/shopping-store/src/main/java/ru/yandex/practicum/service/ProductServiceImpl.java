package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.shoppingStore.PageableDto;
import ru.yandex.practicum.shoppingStore.ProductDto;
import ru.yandex.practicum.shoppingStore.SetProductQuantityStateRequestDto;
import ru.yandex.practicum.shoppingStore.enums.ProductCategory;
import ru.yandex.practicum.shoppingStore.enums.ProductState;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getProductsByCategory(ProductCategory productCategory, PageableDto pageableDto) {
        Pageable pageRequest = PageRequest.of(pageableDto.getPage(), pageableDto.getSize(),
                Sort.by(Sort.DEFAULT_DIRECTION, String.join(",", pageableDto.getSort())));
        List<Product> products = productRepository.findAllByProductCategory(productCategory, pageRequest);
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        } else {
            return productMapper.productsToProductDtos(products);
        }

    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.productDtoToProduct(productDto);
        return productMapper.productToProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product oldProduct = productRepository.findByProductId(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + productDto.getProductId() + " not found")
                );
        Product newProduct = productMapper.productDtoToProduct(productDto);
        newProduct.setProductId(oldProduct.getProductId());
        return productMapper.productToProductDto(productRepository.save(newProduct));
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        Product product = productRepository.findByProductId(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + productId + " not found")
        );
        product.setProductState(ProductState.DEACTIVATE);
        return true;
    }

    @Override
    public boolean updateQuantityState(SetProductQuantityStateRequestDto setProductQuantityStateRequestDto) {
        Product product = productRepository.findByProductId(setProductQuantityStateRequestDto.getProductId())
                .orElseThrow(
                        () -> new ProductNotFoundException("Product with id " +
                                setProductQuantityStateRequestDto.getProductId() + " not found")
                );
        product.setQuantityState(setProductQuantityStateRequestDto.getQuantityState());
        return true;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        Product product = productRepository.findByProductId(productId).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + productId + " not found")
        );
        return productMapper.productToProductDto(product);
    }
}
