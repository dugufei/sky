package com.example.sky.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author sky
 * @version 1.0 on 2017-11-20 上午12:09
 * @see Model
 */
public class Model {

	/**
	 * resources :
	 * {"core":{"limit":60,"remaining":60,"reset":1511111363},"search":{"limit":10,"remaining":10,"reset":1511107823},"graphql":{"limit":0,"remaining":0,"reset":1511111363}}
	 * rate : {"limit":60,"remaining":60,"reset":1511111363}
	 */

	@SerializedName("resources") public ResourcesEntity	resources;

	@SerializedName("rate") public RateEntity			rate;

	public static class ResourcesEntity {

		/**
		 * core : {"limit":60,"remaining":60,"reset":1511111363} search :
		 * {"limit":10,"remaining":10,"reset":1511107823} graphql :
		 * {"limit":0,"remaining":0,"reset":1511111363}
		 */

		@SerializedName("core") public CoreEntity		core;

		@SerializedName("search") public SearchEntity	search;

		@SerializedName("graphql") public GraphqlEntity	graphql;

		public static class CoreEntity {

			/**
			 * limit : 60 remaining : 60 reset : 1511111363
			 */

			@SerializedName("limit") public int		limit;

			@SerializedName("remaining") public int	remaining;

			@SerializedName("reset") public int		reset;
		}

		public static class SearchEntity {

			/**
			 * limit : 10 remaining : 10 reset : 1511107823
			 */

			@SerializedName("limit") public int		limit;

			@SerializedName("remaining") public int	remaining;

			@SerializedName("reset") public int		reset;
		}

		public static class GraphqlEntity {

			/**
			 * limit : 0 remaining : 0 reset : 1511111363
			 */

			@SerializedName("limit") public int		limit;

			@SerializedName("remaining") public int	remaining;

			@SerializedName("reset") public int		reset;
		}
	}

	public static class RateEntity {

		/**
		 * limit : 60 remaining : 60 reset : 1511111363
		 */

		@SerializedName("limit") public int		limit;

		@SerializedName("remaining") public int	remaining;

		@SerializedName("reset") public int		reset;
	}
}
