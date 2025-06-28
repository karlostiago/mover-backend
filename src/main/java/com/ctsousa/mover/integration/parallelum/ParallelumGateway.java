package com.ctsousa.mover.integration.parallelum;

import com.ctsousa.mover.integration.parallelum.domain.*;

import java.time.LocalDate;
import java.util.List;

public interface ParallelumGateway {

    List<Brand> allBrands();

    List<Reference> allReference();

    List<Model> listModels(String codeBrand);

    List<Year> listYear(String codeBrand, String codeModel);

    Brand findBrand(String brandName);

    Year findYear(String codeBrand, String codeModel, Integer modelYear, String fuelType);

    Model findModel(String codeBrand, String modelName);

    Reference findReference(LocalDate reference);

    Fipe findFipe(String codeBrand, String codeModel, String codeYear, String codeReference);
}
