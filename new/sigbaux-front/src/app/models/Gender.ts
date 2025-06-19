import {EGender} from "../interfaces/enum-gender"

export class Gender {
    static female= { value: EGender.FEMALE, label: 'Madame' };
    static Male= { value: EGender.MALE, label: 'Monsieur' };

    static getOptions() {
        return[Gender.Male,Gender.female];
    }

}