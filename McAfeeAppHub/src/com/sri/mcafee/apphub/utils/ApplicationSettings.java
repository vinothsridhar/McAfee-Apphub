package com.sri.mcafee.apphub.utils;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

	private static final String SETTINGS_NAME="AdobeProductChallenge";
	private static SharedPreferences pref=null;

	public static final String PLAYLIST_JSON = "playlist_json";
	public static final String FAVORITE_PLAYER = "favorite_player";
	public static final String SELECTED_PLAYER = "selected_player";

	private static SharedPreferences getSharedPreferences(Context context)
	{
		if(pref==null)
			pref=context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);

		return pref;
	}

	private static SharedPreferences getSharePreferences(Context context)
	{
		return getSharedPreferences(context);
	}

	public static String getPlaylistJson(Context context)
	{
		return getSharePreferences(context).getString(PLAYLIST_JSON,null);
	}

	public static void setPlaylistJson(Context context, String value)
	{
		SharedPreferences.Editor editor=getSharePreferences(context).edit();
		editor.putString(PLAYLIST_JSON, value);
		editor.commit();
	}

	public static Set<String> getFavoritePlayer(Context context) {
		String favoriteString = getSharePreferences(context).getString(FAVORITE_PLAYER, null);
		if (favoriteString == null) {
			return new HashSet<String>();
		}

		String[] favoriteArray = favoriteString.split(",");
		Set<String> favoriteSet = new HashSet<String>();
		for (String str : favoriteArray) {
			favoriteSet.add(str);
		}

		return favoriteSet;
	}

	public static void addFavoritePlayer(Context context, String value)
	{
		Set<String> previous = getFavoritePlayer(context);
		previous.add(value);
		String favoriteString = "";
		for (String str : previous) {
			favoriteString += str + ",";
		}

		L.d("favorite string: " + favoriteString);

		SharedPreferences.Editor editor=getSharePreferences(context).edit();
		editor.putString(FAVORITE_PLAYER, favoriteString);
		editor.commit();
	}

	public static void removeFavoritePlayer(Context context, String value)
	{
		Set<String> previous = getFavoritePlayer(context);
		previous.remove(value);
		String favoriteString = "";
		if (!previous.isEmpty()) {
			for (String str : previous) {
				favoriteString += str + ",";
			}
		}

		L.d("favorite string: " + favoriteString);

		SharedPreferences.Editor editor=getSharePreferences(context).edit();
		editor.putString(FAVORITE_PLAYER, favoriteString);
		editor.commit();
	}

	public static String getSelectedJson(Context context)
	{
		return getSharePreferences(context).getString(SELECTED_PLAYER,null);
	}

	public static void setSelectedJson(Context context, String value)
	{
		SharedPreferences.Editor editor=getSharePreferences(context).edit();
		editor.putString(SELECTED_PLAYER, value);
		editor.commit();
	}

}