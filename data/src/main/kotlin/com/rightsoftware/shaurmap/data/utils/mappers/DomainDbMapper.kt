package com.rightsoftware.shaurmap.data.utils.mappers

import com.rightsoftware.shaurmap.business.Restaurant as DomainRestaurant
import com.rightsoftware.shaurmap.data.Restaurant as DbRestaurant


class DomainDbMapper {
    fun convertToDbModel(domainModel : DomainRestaurant) = with(domainModel){
        DbRestaurant(id, latitude, longitude)
    }

    fun convertToDomainModel(dbModel : DbRestaurant) = with(dbModel){
        DomainRestaurant(id, latitude, longitude)
    }
}