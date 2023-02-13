package com.epam.esm.dao;

import com.epam.esm.entity.Entity;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ErrorCodes;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

@AllArgsConstructor
public abstract class GenericDao<T extends Entity> {

    protected JdbcTemplate jdbcTemplate;

    protected long getGeneratedId(KeyHolder keyHolder) {
        var keyMap = keyHolder.getKeys();
        if (keyMap != null)
            return ((Number) keyMap.get("id")).longValue();
        throw new DaoException(ErrorCodes.SAVE_FAILURE);
    }
}
