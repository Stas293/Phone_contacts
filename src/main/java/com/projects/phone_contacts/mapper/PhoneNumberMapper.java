package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.PhoneCreateUpdateDto;
import com.projects.phone_contacts.model.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for {@link PhoneCreateUpdateDto}
 */
public class PhoneNumberMapper {
    private static final int COUNTRY_CODE_LENGTH = 3;
    private static final int AREA_CODE_LENGTH = 3;

    @Mapper(componentModel = "spring")
    public interface PhoneMapper extends com.projects.phone_contacts.mapper.Mapper<PhoneCreateUpdateDto, Phone> {
        @Mapping(target = "countryCode", source = "phoneNumber", qualifiedByName = "mapCountryCode")
        @Mapping(target = "areaCode", source = "phoneNumber", qualifiedByName = "mapAreaCode")
        @Mapping(target = "number", source = "phoneNumber", qualifiedByName = "mapNumber")
        Phone map(PhoneCreateUpdateDto phoneNumber);

        @Named("mapCountryCode")
        default String mapCountryCode(PhoneCreateUpdateDto phoneNumber) {
            int positionOfFirstZero = phoneNumber.getNumber().indexOf('0');
            return phoneNumber.getNumber().substring(0, positionOfFirstZero + 1);
        }

        @Named("mapAreaCode")
        default String mapAreaCode(PhoneCreateUpdateDto phoneNumber) {
            int positionOfFirstZero = phoneNumber.getNumber().indexOf('0');
            return phoneNumber.getNumber().substring(positionOfFirstZero + 1, positionOfFirstZero + AREA_CODE_LENGTH);
        }

        @Named("mapNumber")
        default String mapNumber(PhoneCreateUpdateDto phoneNumber) {
            return phoneNumber.getNumber().substring(COUNTRY_CODE_LENGTH + AREA_CODE_LENGTH);
        }
    }
}
