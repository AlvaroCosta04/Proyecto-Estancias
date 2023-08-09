package com.estancias.Estancias.services;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.entities.Province;
import com.estancias.Estancias.repositories.ProvinceRepository;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Transactional
    public void create(Province province) {
        provinceRepository.save(province);
    }

    @Transactional
    public void update(Province province, Integer id) throws ServicesException {
        province.setId(id);
        provinceRepository.save(province);
    }

    @Transactional
    public void delete(Integer id) throws ServicesException {
        Province house = findById(id);
        provinceRepository.delete(house);
    }

    @Transactional(readOnly = true)
    public Province findById(@NotNull Integer id) throws ServicesException {
        return provinceRepository.findById(id).orElseThrow(() -> new ServicesException("Province not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<Province> findAllByOrderByNameAsc() {
        return provinceRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Province> findByNameContaining(String name) {
        return provinceRepository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public Province findRandomProvince() {
        return provinceRepository.findRandomProvince();
    }

    @Transactional(readOnly = true)
    public List<Province> find5RandomsProvinces() {
        return provinceRepository.find5RandomsProvinces();
    }

}
