import { City as CityList } from "./city"

export interface CityListPaginated {

    total: string
      pageSize: number
      page: number
      cityList: CityList[]
}
