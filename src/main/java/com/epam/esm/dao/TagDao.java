package com.epam.esm.dao;

import com.epam.esm.dao.request.TagSearchCriteria;
import com.epam.esm.model.Tag;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Tag} in and out
 * of data source.
 *
 * @author Ilya Morozov
 */
public interface TagDao {

    /**
     * Retrieves data of {@code Tag} from
     * data source by it name
     * which equals to {@code String name}.
     *
     * @param name tag name.
     * @return {@code Tag}.
     */
    Tag getByName(String name);

    /**
     * Retrieves data of {@code Tag} from
     * data source by it id
     * which equals to {@code int tagId}.
     *
     * @param tagId tag id.
     * @return {@code Tag}.
     */
    Tag getById(long tagId);

    /**
     * Retrieves certain number of {@code Tag} from data source
     * filtered by {@code TagSearchCriteria}.
     *
     * @param searchCriteria object containing search criteria.
     * @param page page number of {@code Tag} to return.
     * @param size page size of {@code Tag} to return from data source.
     * @return List<Tag> - certain number of tags in data source.
     */
    List<Tag> getAllByPage(TagSearchCriteria searchCriteria, int page, int size);

    /**
     * Retrieves number of a last page from data source if every page
     * contains certain number of {@code Tag}.
     *
     * @param size size of a page.
     * @return number of a last page.
     */
    int getLastPage(int size);

    /**
     * Get the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@link Tag}.
     */
    Tag getMostFrequentFromHighestCostUser();

    /**
     * Adds new {@code Tag} to data source.
     *
     * @param tag {@code Tag} which to be added to data source.
     * @return added {@code Tag} from data source.
     */
    Tag create(Tag tag);

    /**
     * Deletes {@code Tag} from data source by it id
     * which equals to {@code int tagId}.
     *
     * @param tagId id of a {@code Tag} which to delete from data source.
     */
    void deleteById(long tagId);
}
