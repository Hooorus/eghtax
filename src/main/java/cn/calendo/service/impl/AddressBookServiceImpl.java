package cn.calendo.service.impl;

import cn.calendo.common.BaseContext;
import cn.calendo.mapper.AddressBookMapper;
import cn.calendo.pojo.AddressBook;
import cn.calendo.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * add new address
     *
     * @param addressBook
     */
    public void saveAdd(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
    }

    /**
     * set default address
     *
     * @param addressBook
     */
    @Override
    public void setDefaultAdd(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> uw = new LambdaUpdateWrapper<>();
        uw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        uw.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(uw);
        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
    }

    /**
     * get default address
     *
     * @return
     */
    @Override
    public AddressBook getDefaultAdd() {
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        qw.eq(AddressBook::getIsDefault, 1);
        //SQL:select * from address_book where user_id = ? and is_default = 1
        return addressBookService.getOne(qw);
    }

    /**
     * get user's all address
     * @param addressBook
     * @return
     */
    @Override
    public List<AddressBook> getListAdd(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        //条件构造器
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        qw.orderByDesc(AddressBook::getId);
        //SQL:select * from address_book where user_id = ? order by update_time desc
        return this.list(qw);
    }


}
