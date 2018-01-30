import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { AdvertiserRequirement } from './advertiser-requirement.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AdvertiserRequirementService {

    private resourceUrl = 'api/advertiser-requirements';

    constructor(private http: Http) { }

    create(advertiserRequirement: AdvertiserRequirement): Observable<AdvertiserRequirement> {
        const copy = this.convert(advertiserRequirement);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(advertiserRequirement: AdvertiserRequirement): Observable<AdvertiserRequirement> {
        const copy = this.convert(advertiserRequirement);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<AdvertiserRequirement> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(advertiserRequirement: AdvertiserRequirement): AdvertiserRequirement {
        const copy: AdvertiserRequirement = Object.assign({}, advertiserRequirement);
        return copy;
    }
}
