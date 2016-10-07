package wsvintsitsky.shortener.dataaccess.network.impl;

import java.util.List;

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.dataaccess.network.dtobject.RequestCommand;
import wsvintsitsky.shortener.dataaccess.network.transmitting.Transmitter;
import wsvintsitsky.shortener.datamodel.Tag;

public class TagDaoNetworkImpl extends AbstractDaoNetworkImpl implements TagDao {

	@Override
	public List<Tag> getAll() {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_GET_ALL);
		List<Tag> tags = (List<Tag>) response;
		return tags;
	}

	@Override
	public Tag get(Long id) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_GET, id);
		Tag tag = (Tag) response;
		return tag;
	}

	@Override
	public Tag insert(Tag entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_INSERT, entity);
		Tag tag = (Tag) response;
		return tag;
	}

	@Override
	public Tag update(Tag entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_UPDATE, entity);
		Tag tag = (Tag) response;
		return tag;
	}

	@Override
	public void delete(Long id) {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_DELETE, id);
	}

	@Override
	public void deleteAll() {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_DELETE_ALL);
	}

	@Override
	public List<Tag> getExistingTags(List<String> tagDescriptions) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_UPDATE, tagDescriptions);
		List<Tag> tags = (List<Tag>) response;
		return tags;
	}

	@Override
	public Tag getTagByTagDescription(String tagDescription) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_GET_BY_TAG_DESCRIPTION, tagDescription);
		Tag tag = (Tag) response;
		return tag;
	}

	@Override
	public List<Tag> getTagsByIds(List<Long> tagsIds) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.TAG_GET_BY_IDS, tagsIds);
		List<Tag> tags = (List<Tag>) response;
		return tags;
	}

}
