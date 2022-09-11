package cn.calendo.service;

import cn.calendo.pojo.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {

    //add new address
    void saveAdd(AddressBook addressBook);

    //set default address
    void setDefaultAdd(AddressBook addressBook);

    //get default address
    AddressBook getDefaultAdd();

    //select user's all address
    List<AddressBook> getListAdd(AddressBook addressBook);
}
