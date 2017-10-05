package com.tokopedia.discovery.intermediary.data.mapper;

import com.tokopedia.core.network.entity.intermediary.Badge;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.Image;
import com.tokopedia.core.network.entity.intermediary.Label;
import com.tokopedia.core.network.entity.intermediary.Product;
import com.tokopedia.core.network.entity.intermediary.Section;
import com.tokopedia.core.network.entity.hotlist.HotListResponse;
import com.tokopedia.core.network.entity.intermediary.brands.Brand;
import com.tokopedia.core.network.entity.intermediary.brands.MojitoBrandsModel;
import com.tokopedia.discovery.intermediary.domain.model.BadgeModel;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;
import com.tokopedia.discovery.intermediary.domain.model.BrandModel;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.intermediary.domain.model.LabelModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;
import com.tokopedia.discovery.intermediary.domain.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func3;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryCategoryMapper implements Func3<Response<CategoryHadesModel>,
        Response<HotListResponse>, Response<MojitoBrandsModel>, IntermediaryCategoryDomainModel> {

    @Override
    public IntermediaryCategoryDomainModel call(Response<CategoryHadesModel> categoryHadesModelResponse,
                                                Response<HotListResponse> hotListResponseResponse,
                                                Response<MojitoBrandsModel> mojitoBrandsModelResponse) {
        IntermediaryCategoryDomainModel intermediaryCategoryDomainModel = new IntermediaryCategoryDomainModel();

        if (categoryHadesModelResponse.body().getData().getIsIntermediary() &&
                getIntermediaryTemplate(categoryHadesModelResponse.body(),intermediaryCategoryDomainModel)) {
            intermediaryCategoryDomainModel.setIntermediary(true);
            intermediaryCategoryDomainModel.setDepartementId(categoryHadesModelResponse.body().getData().getId());
            intermediaryCategoryDomainModel.setHeaderModel(mapHeaderModel(categoryHadesModelResponse.body()));
            intermediaryCategoryDomainModel.setChildCategoryModelList(mapCategoryChildren(categoryHadesModelResponse.body()));
            intermediaryCategoryDomainModel.setCuratedSectionModelList(mapCuration(categoryHadesModelResponse.body()));
            intermediaryCategoryDomainModel.setBannerModelList(mapBanner(categoryHadesModelResponse.body()));
            if (categoryHadesModelResponse.body().getData().getVideo()!=null) {
                intermediaryCategoryDomainModel.setVideoModel(mapVideo(categoryHadesModelResponse.body()));
            }
            intermediaryCategoryDomainModel.setHotListModelList(mapHotList(hotListResponseResponse.body()));
            if (mojitoBrandsModelResponse!=null) {
                intermediaryCategoryDomainModel.setBrandModelList(mapBrands(mojitoBrandsModelResponse.body()));
            }
        }
        return  intermediaryCategoryDomainModel;
    }

    private HeaderModel mapHeaderModel(CategoryHadesModel categoryHadesModel) {

        HeaderModel headerModel = new HeaderModel();
        if (categoryHadesModel.getData()!=null) {
            headerModel = new HeaderModel(categoryHadesModel.getData().getName(),
                    categoryHadesModel.getData().getHeaderImage());
        }
        return headerModel;
    }

    private List<ChildCategoryModel> mapCategoryChildren(CategoryHadesModel categoryHadesModel) {

        List<ChildCategoryModel> categoryModelList = new ArrayList<>();
        if (categoryHadesModel.getData()!=null && categoryHadesModel.getData().getChild()!=null) {
            for (Child child: categoryHadesModel.getData().getChild()) {
                ChildCategoryModel childCategoryModel = new ChildCategoryModel();
                childCategoryModel.setCategoryId(child.getId());
                childCategoryModel.setCategoryImageUrl(child.getThumbnailImage());
                childCategoryModel.setCategoryName(child.getName());
                categoryModelList.add(childCategoryModel);
            }
        }
        return categoryModelList;
    }

    private List<CuratedSectionModel> mapCuration(CategoryHadesModel categoryHadesModel) {

        List<CuratedSectionModel> curatedSectionModels = new ArrayList<>();
        if (categoryHadesModel.getData() !=null && categoryHadesModel.getData().getCuratedProduct()
                !=null && categoryHadesModel.getData().getCuratedProduct().getSections() !=null) {
            for (Section section: categoryHadesModel.getData().getCuratedProduct().getSections()) {
                CuratedSectionModel curatedSectionModel = new CuratedSectionModel();
                curatedSectionModel.setTitle(section.getTitle());


                List<ProductModel> productModels = new ArrayList<>();

                if (section.getProducts()!=null) {
                    for (Product product: section.getProducts()) {
                        ProductModel productModel = new ProductModel();
                        productModel.setName(product.getName());
                        productModel.setId(product.getId());
                        productModel.setImageUrl(product.getImageUrl());
                        productModel.setPrice(product.getPrice());
                        productModel.setShopName(product.getShop().getName());
                        productModel.setShopLocation(product.getShop().getLocation());
                        List<BadgeModel> badgeModels = new ArrayList<>();
                        for (Badge badge: product.getBadges()) {
                            badgeModels.add(new BadgeModel(badge.getImageUrl(),badge.getTitle()));
                        }
                        productModel.setBadges(badgeModels);
                        List<LabelModel> labelModels = new ArrayList<>();
                        for (Label label: product.getLabels()) {
                            labelModels.add(new LabelModel(label.getColor(),label.getTitle()));
                        }
                        productModel.setLabels(labelModels);
                        productModels.add(productModel);

                        if (productModels.size()==6)
                            break;
                    }
                }
                curatedSectionModel.setProducts(productModels);

                curatedSectionModels.add(curatedSectionModel);
            }
        }
        return curatedSectionModels;
    }

    private List<HotListModel> mapHotList(HotListResponse hotListResponse) {
        List<HotListModel> hotListModels = new ArrayList<>();
        if (hotListResponse==null) return hotListModels;
        for (com.tokopedia.core.network.entity.hotlist.List list: hotListResponse.getList()) {
            HotListModel hotListModel = new HotListModel();
            hotListModel.setId(list.getHotProductId());
            hotListModel.setImageUrl(list.getImgPortrait()==null ? "" :list.getImgPortrait().get280x418());
            hotListModel.setImageUrlBanner(list.getImgShare()==null ? "" :list.getImg().get375x200());
            hotListModel.setImageUrlSquare(list.getImgSquare()==null ? "" :list.getImgSquare().get200x200());
            hotListModel.setTitle(list.getTitle());
            hotListModel.setUrl(list.getUrl());
            hotListModels.add(hotListModel);
        }
        return  hotListModels;
    }

    private List<BannerModel> mapBanner(CategoryHadesModel categoryHadesModel) {

        List<BannerModel> bannerModels = new ArrayList<>();
        if (categoryHadesModel.getData().getBanner()!=null && categoryHadesModel.getData().getBanner().getImages()!=null) {
            for (Image image: categoryHadesModel.getData().getBanner().getImages()) {
                BannerModel bannerModel = new BannerModel();
                bannerModel.setUrl(image.getUrl());
                bannerModel.setImageUrl(image.getImageUrl());
                bannerModel.setPosition(image.getPosition());
                bannerModels.add(bannerModel);
            }
        }
        return  bannerModels;
    }


    private List<BrandModel> mapBrands(MojitoBrandsModel mojitoBrandsModel) {

        List<BrandModel> brandModels = new ArrayList<>();
        if (mojitoBrandsModel.getData()!=null && mojitoBrandsModel.getData().size()>0) {
            for (Brand brand: mojitoBrandsModel.getData()) {
                BrandModel brandModel = new BrandModel();
                brandModel.setId(String.valueOf(brand.getShopId()));
                brandModel.setImageUrl(brand.getLogoUrl());
                brandModel.setBrandName(brand.getShopName());
                brandModels.add(brandModel);
            }
        }
        return  brandModels;
    }

    private VideoModel mapVideo(CategoryHadesModel categoryHadesModel) {

        VideoModel videoModel = new VideoModel();
        videoModel.setTitle(categoryHadesModel.getData().getVideo().getTitle());
        videoModel.setDescription(categoryHadesModel.getData().getVideo().getDescription());
        videoModel.setVideoUrl(categoryHadesModel.getData().getVideo().getVideoUrl());

        return  videoModel;
    }

    private boolean getIntermediaryTemplate(CategoryHadesModel categoryHadesModel,
                                            IntermediaryCategoryDomainModel intermediaryCategoryDomainModel) {
        if (categoryHadesModel.getData().getTemplate()==null) return false;
        switch (categoryHadesModel.getData().getTemplate()) {
            case IntermediaryCategoryDomainModel.LIFESTYLE_TEMPLATE:
                intermediaryCategoryDomainModel.setTemplate(IntermediaryCategoryDomainModel.LIFESTYLE_TEMPLATE);
                return true;
          /*  BELOW IS COMMENTED FOR NEXT RELEASE

            case IntermediaryCategoryDomainModel.TECHNOLOGY_TEMPLATE:
                intermediaryCategoryDomainModel.setTemplate(IntermediaryCategoryDomainModel.LIFESTYLE_TEMPLATE);
                return true;
            case IntermediaryCategoryDomainModel.DEFAULT_TEMPLATE:
                intermediaryCategoryDomainModel.setTemplate(IntermediaryCategoryDomainModel.DEFAULT_TEMPLATE);
                return true;*/
           default:
               return false;
        }
    }


}
