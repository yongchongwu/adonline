import { BaseEntity } from './../../shared';

export class Material implements BaseEntity {
    constructor(
        public id?: number,
        public position?: string,
        public tags?: string,
        public startDate?: string,
        public endDate?: string,
        public imgUrl?: string,
        public hrefUrl?: string,
        public customerPrice?: number,
        public copy?: string,
        public collectDate?: string,
    ) {
    }
}
