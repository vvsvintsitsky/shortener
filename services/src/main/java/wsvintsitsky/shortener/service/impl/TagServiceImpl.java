package wsvintsitsky.shortener.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.Url2TagDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.service.TagService;

@Service
public class TagServiceImpl implements TagService {

	@Inject
	private TagDao tagDao;

	@Inject
	private Url2TagDao url2TagDao;

	@Override
	@Transactional
	public void saveOrUpdate(Tag tag) {
		if (tag.getId() == null) {
			tagDao.insert(tag);
			url2TagDao.insertUrl2Tags(tag);
		} else {
			tagDao.update(tag);
			updateUrl2Tag(tag);
		}
	}

	private void updateUrl2Tag(Tag tag) {
		List<Long> exUrl2Tags = url2TagDao.getMatchingUrls2Tags(tag);
		List<Long> insUrl2Tags = new ArrayList<Long>();
		if(tag.getUrls() != null) {
			setUrl2TagsForDeleteAndInsert(tag, exUrl2Tags, insUrl2Tags);
			if(exUrl2Tags.size() != 0) {
				url2TagDao.deleteUrl2Tags(tag, exUrl2Tags);
			}
			if(insUrl2Tags.size() != 0) {
				url2TagDao.insertUrl2Tags(tag, insUrl2Tags);
			}
		}
	}

	private void setUrl2TagsForDeleteAndInsert(Tag tag, List<Long> exUrl2Tags, List<Long> insUrl2Tags) {
		for (Url url : tag.getUrls()) {
			if (exUrl2Tags.contains(url.getId())) {
				exUrl2Tags.remove(url.getId());
			} else {
				insUrl2Tags.add(url.getId());
			}
		}
	}

	@Override
	public Tag get(Long id) {
		return tagDao.get(id);
	}

	@Override
	public List<Tag> getAll() {
		return tagDao.getAll();
	}

	@Override
	@Transactional
	public void delete(Tag tag) {
		url2TagDao.deleteUrl2Tags(tag);
		tagDao.delete(tag.getId());
	}

	@Override
	@Transactional
	public void deleteAll() {
		url2TagDao.deleteAll();
		tagDao.deleteAll();
	}

	@Override
	public Tag getTagWithUrls(String tagDescription) {
		return tagDao.getTagWithUrls(tagDescription);
	}

	@Override
	public List<Tag> getExistingTags(List<String> tagDescriptions) {
		return tagDao.getExistingTags(tagDescriptions);
	}

}
