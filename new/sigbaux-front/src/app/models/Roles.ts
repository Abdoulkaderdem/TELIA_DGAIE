import {ERoles} from "../interfaces/enum-roles"

export class Roles {
    static Admin= { value: ERoles.ADMIN, label: 'Administrateur' };
    static Public= { value: ERoles.PUBLIC, label: 'Public' };

    static getOptions() {
        return[
            Roles.Admin,
            Roles.Public
        ];
    }
}