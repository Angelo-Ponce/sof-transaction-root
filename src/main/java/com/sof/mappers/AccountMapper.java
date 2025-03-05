package com.sof.mappers;

import com.sof.dto.AccountDTO;
import com.sof.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO toAccountDTO(Account account);

    Account toAccount(AccountDTO accountDTO);
}
