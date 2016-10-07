package wsvintsitsky.shortener.dataaccess.network.impl;

import java.util.List;

import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.dataaccess.network.dtobject.RequestCommand;
import wsvintsitsky.shortener.dataaccess.network.transmitting.Transmitter;
import wsvintsitsky.shortener.datamodel.Url;

public class UrlDaoNetworkImpl extends AbstractDaoNetworkImpl implements UrlDao {

	@Override
	public List<Url> getAll() {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET_ALL);
		List<Url> urls = (List<Url>) response;
		return urls;
	}

	@Override
	public Url get(Long id) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET, id);
		Url url = (Url) response;
		return url;
	}

	@Override
	public Url insert(Url entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_INSERT, entity);
		Url url = (Url) response;
		return url;
	}

	@Override
	public Url update(Url entity) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_UPDATE, entity);
		Url url = (Url) response;
		return url;
	}

	@Override
	public void delete(Long id) {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_DELETE, id);
	}

	@Override
	public void deleteAll() {
		Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_DELETE_ALL);
	}

	@Override
	public Url getUrlByShortUrl(String shortUrl) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET_URL_BY_SHORTURL, shortUrl);
		Url url = (Url) response;
		return url;
	}

	@Override
	public List<Url> getUrlsByAccountId(Long accountId, Integer limit, Integer offset) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET_URLS_BY_ACCOUNT_ID, accountId, limit, offset);
		List<Url> urls = (List<Url>) response;
		return urls;
	}

	@Override
	public Url checkOwnership(Long accountId, String shortUrl) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET, accountId, shortUrl);
		Url url = (Url) response;
		return url;
	}

	@Override
	public List<Url> getUrlsByIds(List<Long> urlsIds) {
		Object response = Transmitter.executeRequest(getAddress(), getPort(), RequestCommand.URL_GET_URLS_BY_IDS, urlsIds);
		List<Url> urls = (List<Url>) response;
		return urls;
	}

}
