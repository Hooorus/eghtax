package cn.calendo.controller;

import cn.calendo.common.R;
import cn.calendo.pojo.AddressBook;
import cn.calendo.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookControler {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * add new address
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBookService.saveAdd(addressBook);
        return R.success(addressBook);
    }

    /**
     * set default address
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefaultAdd(addressBook);
        return R.success(addressBook);
    }

    /**
     * save address
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * select address by id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * select default address
     *
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        AddressBook addressBook = addressBookService.getDefaultAdd();
        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * select user's all address
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        List<AddressBook> listAdd = addressBookService.getListAdd(addressBook);
        return R.success(listAdd);
    }
}
