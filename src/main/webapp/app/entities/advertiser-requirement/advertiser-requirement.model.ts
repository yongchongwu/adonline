import { BaseEntity } from './../../shared';

export class AdvertiserRequirement implements BaseEntity {
    constructor(
        public id?: number,
        public masterId?: number,
        public minAmount?: number,
        public meid?: string,
        public orderNo?: string,
        public wuid?: string,
        public city?: string,
        public category?: string,
        public date?: string,
        public time?: string,
        public clicked?: boolean,
        public overAvgAmount?: boolean,
        public os?: string,
        public network?: string,
        public sex?: string,
        public tradeType?: string,
    ) {
        this.clicked = false;
        this.overAvgAmount = false;
    }
}
