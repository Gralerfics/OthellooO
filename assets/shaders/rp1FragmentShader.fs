#version 430 core

in vec3 pos;
out vec4 fragColor;

// ================================ Uniforms ================================ //

uniform int hres, vres;
uniform int hdrRes;
uniform int frameCnt;
uniform int TDEPTH;

uniform int trianglesNum;
uniform int dytrisNum;
uniform int pointNum;

uniform samplerBuffer triangles;
uniform samplerBuffer tree;
uniform samplerBuffer dytris;
uniform samplerBuffer dytree;
uniform samplerBuffer surpnter;
uniform samplerBuffer lpinf;
uniform sampler2D lastFrame;
uniform sampler2D hdrMap;
uniform sampler2D hdrCache;

uniform float lightFactor;
uniform int hdrClosed;
uniform int chessSpClosed;

uniform vec3 eye;
uniform vec3 u, v, w;

uniform float rdSeed[4];
uniform int selected;
uniform int lastpos;

// ================================ Variable ================================ //

int rdCnt = 0;

// ================================ Constant ================================ //

#define SAMPLE_TIMES 30
#define ENABLE_SOBOL 1

const float PI = 3.1415926;
const float invPI = 0.3183099;
const float INF = 1e9;
const float EPS = 1e-5;
const int MAX_DEPTH = 10;
const int TRIANGLE_SIZE = 12;
const int BVHNODE_SIZE = 4;

// ================================= Struct ================================= //

struct Ray {
    vec3 origin;
    vec3 direction;
};

struct Material {
    vec3 emissive;
    vec3 plainColor;
    float subsurface;
    float metallic;
    float specular, specularTint;
    float roughness;
    float anisotropic;
    float sheen, sheenTint;
    float clearcoat, clearcoatGloss;
    float IOR;
    // float transmission;
    int tag;
};

struct Triangle {
    vec3 p1, p2, p3;
    vec3 n1, n2, n3;
};

struct BVHNode {
    int left, right;
    int n, index;
    vec3 AA, BB;
};

struct HitRst {
    bool isHit;
    bool isInside;
    vec3 hitPoint;
    float t;
    vec3 normal;
    vec3 dir;
    Material material;
};

// ================================ Function ================================ //

//                                    随机                                     //

float _rand(float x, float y){
    return fract(cos(dot(vec2(x,y), vec2(19.2828, 3.1616))) * 45738.5354);
}

float rand(){
    return _rand(_rand(rdSeed[3], _rand(pos.x, rdSeed[0])), _rand(_rand(rdSeed[1], pos.y), _rand(rdCnt ++, rdSeed[2])));
}

vec2 randInSquare() {
    return vec2(rand(), rand());
}

//                                    采样                                     //

float sqr(float x) {
    return x*x;
}

uint hash(inout uint seed) {
    seed = uint(seed ^ uint(61)) ^ uint(seed >> uint(16));
    seed *= uint(9);
    seed = seed ^ (seed >> 4);
    seed *= uint(0x27d4eb2d);
    seed = seed ^ (seed >> 15);
    return seed;
}

vec2 CranleyPattersonRotation(vec2 p) {
    uint pseed = uint(uint((pos.x * 0.5 + 0.5) * hres) * uint(1973) + uint((pos.y * 0.5 + 0.5) * vres) * uint(9277) + uint(324377/1927) * uint(23339)) | uint(1);
    float u = float(hash(pseed)) / 4294967296.0;
    float v = float(hash(pseed)) / 4294967296.0;
    p.x += u;
    if (p.x > 1) p.x -= 1;
    if (p.x < 0) p.x += 1;
    p.y += v;
    if (p.y > 1) p.y -= 1;
    if (p.y < 0) p.y += 1;
    return p;
}

const uint V[8 * 32] = {
    2147483648, 1073741824, 536870912, 268435456, 134217728, 67108864, 33554432, 16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1,
    2147483648, 3221225472, 2684354560, 4026531840, 2281701376, 3422552064, 2852126720, 4278190080, 2155872256, 3233808384, 2694840320, 4042260480, 2290614272, 3435921408, 2863267840, 4294901760, 2147516416, 3221274624, 2684395520, 4026593280, 2281736192, 3422604288, 2852170240, 4278255360, 2155905152, 3233857728, 2694881440, 4042322160, 2290649224, 3435973836, 2863311530, 4294967295,
    2147483648, 3221225472, 1610612736, 2415919104, 3892314112, 1543503872, 2382364672, 3305111552, 1753219072, 2629828608, 3999268864, 1435500544, 2154299392, 3231449088, 1626210304, 2421489664, 3900735488, 1556135936, 2388680704, 3314585600, 1751705600, 2627492864, 4008611328, 1431684352, 2147543168, 3221249216, 1610649184, 2415969680, 3892340840, 1543543964, 2382425838, 3305133397,
    2147483648, 3221225472, 536870912, 1342177280, 4160749568, 1946157056, 2717908992, 2466250752, 3632267264, 624951296, 1507852288, 3872391168, 2013790208, 3020685312, 2181169152, 3271884800, 546275328, 1363623936, 4226424832, 1977167872, 2693105664, 2437829632, 3689389568, 635137280, 1484783744, 3846176960, 2044723232, 3067084880, 2148008184, 3222012020, 537002146, 1342505107,
    2147483648, 1073741824, 536870912, 2952790016, 4160749568, 3690987520, 2046820352, 2634022912, 1518338048, 801112064, 2707423232, 4038066176, 3666345984, 1875116032, 2170683392, 1085997056, 579305472, 3016343552, 4217741312, 3719483392, 2013407232, 2617981952, 1510979072, 755882752, 2726789248, 4090085440, 3680870432, 1840435376, 2147625208, 1074478300, 537900666, 2953698205,
    2147483648, 1073741824, 1610612736, 805306368, 2818572288, 335544320, 2113929216, 3472883712, 2290089984, 3829399552, 3059744768, 1127219200, 3089629184, 4199809024, 3567124480, 1891565568, 394297344, 3988799488, 920674304, 4193267712, 2950604800, 3977188352, 3250028032, 129093376, 2231568512, 2963678272, 4281226848, 432124720, 803643432, 1633613396, 2672665246, 3170194367,
    2147483648, 3221225472, 2684354560, 3489660928, 1476395008, 2483027968, 1040187392, 3808428032, 3196059648, 599785472, 505413632, 4077912064, 1182269440, 1736704000, 2017853440, 2221342720, 3329785856, 2810494976, 3628507136, 1416089600, 2658719744, 864310272, 3863387648, 3076993792, 553150080, 272922560, 4167467040, 1148698640, 1719673080, 2009075780, 2149644390, 3222291575,
    2147483648, 1073741824, 2684354560, 1342177280, 2281701376, 1946157056, 436207616, 2566914048, 2625634304, 3208642560, 2720006144, 2098200576, 111673344, 2354315264, 3464626176, 4027383808, 2886631424, 3770826752, 1691164672, 3357462528, 1993345024, 3752330240, 873073152, 2870150400, 1700563072, 87021376, 1097028000, 1222351248, 1560027592, 2977959924, 23268898, 437609937
};

uint grayCode(uint i) {
	return i ^ (i >> 1);
}

float sobol(uint d, uint i) {
    uint result = 0;
    uint offset = d * 32;
    for (uint j = 0; i != 0; i >>= 1, j ++) {
        if ((i & 1) != 0) {
            result ^= V[j + offset];
        }
    }
    return float(result) * (1.0f / float(0xFFFFFFFFU));
}

vec2 sobolVec2(uint i, uint b) {
    float u = sobol(b * 2, grayCode(i));
    float v = sobol(b * 2 + 1, grayCode(i));
    vec2 jittered = CranleyPattersonRotation(vec2(u, v));
    return jittered;
}

#if ENABLE_SOBOL
    vec3 hemisphereSampler(float x1, float x2) {
        float z = x1;
        float r = max(0, sqrt(1.0 - z * z));
        float theta = 2.0 * PI * x2;
        return vec3(r * cos(theta), r * sin(theta), z);
    }
#else
    vec3 hemisphereSampler() {
        float r = sqrt(rand());
        float theta = rand() * 2.0 * PI;
        float x = r * cos(theta);
        float y = r * sin(theta);
        float z = sqrt(max(0.0, 1.0 - x * x - y * y));
        return vec3(x, y, z);
    }
#endif

vec3 hemisphereToNormal(vec3 v, vec3 normal) {
    vec3 t = vec3(1.0, 0.0, 0.0);
    if (abs(normal.x) > 1 - EPS) t = vec3(0, 0, 1);
    vec3 tg = normalize(cross(normal, t));
    vec3 bt = normalize(cross(normal, tg));
    return v.x * tg + v.y * bt + v.z * normal;
}

vec3 cosineHemisphereSampler(float x1, float x2, vec3 N) {
    float r = sqrt(abs(x1));
    float theta = x2 * 2.0 * PI;
    float x = r * cos(theta);
    float y = r * sin(theta);
    float z = sqrt(abs(1.0 - x * x - y * y));
    vec3 L = hemisphereToNormal(vec3(x, y, z), N);
    return L;
}

vec2 SphereSampler(vec3 v) {
    vec2 rst = vec2(atan(v.z, v.x) / 2.0 / PI + 0.5, asin(v.y) / PI + 0.5);
    rst.y = 1.0 - rst.y;
    return rst;
}

vec2 SphericalMapSampler(vec3 v) {
    vec2 uv = vec2(atan(v.z, v.x), asin(v.y));
    uv /= vec2(2.0 * PI, PI);
    uv += 0.5;
    uv.y = 1.0 - uv.y;
    return uv;
}

float hdrPdf(vec3 L, int hdrResolution) {
//     float pdf = 0.5;
//     float theta = PI * (0.5 - L.y);
//     float sin_theta = max(sin(theta), 1e-10);
//     float p_convert = float(hdrResolution * hdrResolution / 2) / max(0.0001, 2.0 * PI * PI * sin_theta);
//     return pdf * p_convert;

    vec2 uv = SphericalMapSampler(normalize(L));
    float pdf = texture2D(hdrCache, uv).b;
    float theta = PI * (0.5 - uv.y);
    float sin_theta = max(sin(theta), 1e-10);
    float p_convert = float(hdrResolution * hdrResolution / 2) / (2.0 * PI * PI * sin_theta);
    return pdf * p_convert;
}

vec3 hdrColor(vec3 v) {
    if (hdrClosed == 1) {
        return mix(vec3(0.1, 0.6, 1.0), vec3(1.0), max(0.01, (-v.y + 1.0) / 2.0));
    } else {
        vec2 uv = SphericalMapSampler(normalize(v));
        vec3 color = texture2D(hdrMap, uv).rgb * lightFactor;
        return color;
    }
}

vec3 bkSampler(float xi_1, float xi_2) {
    if (hdrClosed == 1) {
        return mix(vec3(0.1, 0.6, 1.0), vec3(1.0), max(0.01, (-v.y + 1.0) / 2.0));
    } else {
        vec2 xy = texture2D(hdrCache, vec2(xi_1, xi_2)).rg;
        xy.y = 1.0 - xy.y;
        float phi = 2.0 * PI * (xy.x - 0.5);
        float theta = PI * (xy.y - 0.5);
        vec3 L = vec3(cos(theta) * cos(phi), sin(theta), cos(theta) * sin(phi));
        return vec3(0,0,0);
        return L;
    }
}

//                                    解码                                     //

Triangle decodeTriangle(int i, samplerBuffer targetsampler) {
    int offset = TRIANGLE_SIZE * i;
    Triangle rst;
    rst.p1 = texelFetch(targetsampler, offset + 0).xyz;
    rst.p2 = texelFetch(targetsampler, offset + 1).xyz;
    rst.p3 = texelFetch(targetsampler, offset + 2).xyz;
    rst.n1 = texelFetch(targetsampler, offset + 3).xyz;
    rst.n2 = texelFetch(targetsampler, offset + 4).xyz;
    rst.n3 = texelFetch(targetsampler, offset + 5).xyz;
    return rst;
}

Material decodeMaterial(int i, samplerBuffer targetsampler) {
    int offset = TRIANGLE_SIZE * i;
    Material rst;
    vec3 param;
    rst.emissive = texelFetch(targetsampler, offset + 6).xyz;
    rst.plainColor = texelFetch(targetsampler, offset + 7).xyz;
    param = texelFetch(targetsampler, offset + 8).xyz;
    rst.subsurface = param.x;
    rst.metallic = param.y;
    rst.specular = param.z;
    param = texelFetch(targetsampler, offset + 9).xyz;
    rst.specularTint = param.x;
    rst.roughness = param.y;
    rst.anisotropic = param.z;
    param = texelFetch(targetsampler, offset + 10).xyz;
    rst.sheen = param.x;
    rst.sheenTint = param.y;
    rst.clearcoat = param.z;
    param = texelFetch(targetsampler, offset + 11).xyz;
    rst.clearcoatGloss = param.x;
    rst.IOR = param.y;
//     rst.transmission = param.z;
    rst.tag = int(param.z);
    return rst;
}

BVHNode decodeBVHNode(int i, samplerBuffer targetsampler) {
    int offset = BVHNODE_SIZE * i;
    BVHNode rst;
    vec3 children = texelFetch(targetsampler, offset + 0).xyz;
    vec3 leafInf = texelFetch(targetsampler, offset + 1).xyz;
    rst.left = int(children.x);
    rst.right = int(children.y);
    rst.n = int(leafInf.x);
    rst.index = int(leafInf.y);
    rst.AA = texelFetch(targetsampler, offset + 2).xyz;
    rst.BB = texelFetch(targetsampler, offset + 3).xyz;
    return rst;
}

//                                    求交                                     //

HitRst hitTriangle(Ray ray, Triangle tri) {
    HitRst rst;
    rst.t = INF;
    rst.isHit = false;

    vec3 normal = normalize(cross(tri.p2 - tri.p1, tri.p3 - tri.p1));
    if (dot(normal, ray.direction) > 0.0f) {
        rst.isInside = true;
        normal = -normal;
    } else {
        rst.isInside = false;
    }

    if (abs(dot(normal, ray.direction)) < EPS) return rst; // 平行

    float DDN = dot(ray.direction, normal);
    if (abs(DDN) < 0.0001) DDN = 0.0001;
    float t = (dot(normal, tri.p1) - dot(ray.origin, normal)) / DDN;
    if (t < EPS) return rst;
    vec3 I = ray.origin + ray.direction * t;

    vec3 d1 = cross(tri.p2 - tri.p1, I - tri.p1);
    vec3 d2 = cross(tri.p3 - tri.p2, I - tri.p2);
    vec3 d3 = cross(tri.p1 - tri.p3, I - tri.p3);
    if ((dot(d1, normal) < 0 && dot(d2, normal) < 0 && dot(d3, normal) < 0) || (dot(d1, normal) > 0 && dot(d2, normal) > 0 && dot(d3, normal) > 0) ) {
        rst.isHit = true;
        rst.hitPoint = I;
        rst.t = t;
        rst.dir = ray.direction;
        // 插值平滑法线
        float a = ( -(I.x - tri.p2.x) * (tri.p3.y - tri.p2.y) + (I.y - tri.p2.y) * (tri.p3.x - tri.p2.x) )
                / max(0.0001, -(tri.p1.x - tri.p2.x - EPS) * (tri.p3.y - tri.p2.y + EPS) + (tri.p1.y - tri.p2.y + EPS) * (tri.p3.x - tri.p2.x + EPS) );
        float b = ( -(I.x - tri.p3.x) * (tri.p1.y - tri.p3.y) + (I.y - tri.p3.y) * (tri.p1.x - tri.p3.x) )
                / max(0.0001, -(tri.p2.x - tri.p3.x - EPS) * (tri.p1.y - tri.p3.y + EPS) + (tri.p2.y - tri.p3.y + EPS) * (tri.p1.x - tri.p3.x + EPS) );
        float c = 1.0 - a - b;
        rst.normal = normalize(a * tri.n1 + b * tri.n2 + c * tri.n3);
        if (rst.isInside) rst.normal = -rst.normal;
    }

    return rst;
}

// 无交返回 -1
float hitAABB(Ray ray, vec3 AA, vec3 BB) {
    if (abs(ray.direction.x) < 0.0001) ray.direction.x = 0.0001;
    if (abs(ray.direction.y) < 0.0001) ray.direction.y = 0.0001;
    if (abs(ray.direction.z) < 0.0001) ray.direction.z = 0.0001;
    vec3 invD = 1.0f / ray.direction;
    vec3 a = (AA - ray.origin) * invD;
    vec3 b = (BB - ray.origin) * invD;
    vec3 tmax = max(a, b);
    vec3 tmin = min(a, b);
    float A = min(tmax.x, min(tmax.y, tmax.z));
    float B = max(tmin.x, min(tmin.y, tmin.z));
    return (A >= B) ? ((B > 0.0) ? B : A) : (-1);
}

//                                    遍历                                     //

HitRst hitInterval(Ray ray, int l, int r, samplerBuffer targetsampler) {
    HitRst rst;
    rst.isHit = false;
    rst.t = INF;
    for (int i = l; i <= r; i ++) {
        Triangle tri = decodeTriangle(i, targetsampler);
        HitRst rs = hitTriangle(ray, tri);
        if (rs.isHit && rs.t < rst.t) {
            rst = rs;
            rst.material = decodeMaterial(i, targetsampler);
        }
    }
    return rst;
}

HitRst hitBVHTree(Ray ray, samplerBuffer targetsampler, samplerBuffer trisampler) {
    HitRst rst;
    rst.isHit = false;
    rst.t = INF;

    int stack[300];
    int cnt = 0;

    stack[cnt ++] = 0;
    while (cnt > 0) {
        int top = stack[-- cnt];
        BVHNode cur = decodeBVHNode(top, targetsampler);
        // 叶节点
        if (cur.n > 0 || (cur.left < 0 && cur.right < 0)) {
            HitRst rs = hitInterval(ray, cur.index, cur.index + cur.n - 1, trisampler);
            if (rs.isHit && rs.t < rst.t) rst = rs;
            continue;
        }
        // 左右包围盒求交
        float d1 = INF, d2 = INF;
        if (cur.left > 0) {
            BVHNode lchild = decodeBVHNode(cur.left, targetsampler);
            d1 = hitAABB(ray, lchild.AA, lchild.BB);
        }
        if (cur.right > 0) {
            BVHNode rchild = decodeBVHNode(cur.right, targetsampler);
            d2 = hitAABB(ray, rchild.AA, rchild.BB);
        }
        // 遍历最近
        if (d1 > 0.0 && d2 > 0.0) {
            if (d1 < d2) {
                stack[cnt ++] = cur.right;
                stack[cnt ++] = cur.left;
            } else {
                stack[cnt ++] = cur.left;
                stack[cnt ++] = cur.right;
            }
        } else if (d1 > 0.0) {
            stack[cnt ++] = cur.left;
        } else if (d2 > 0.0) {
            stack[cnt ++] = cur.right;
        }
    }

    return rst;
}

//                                    BRDF                                     //

float SchlickFresnel(float u) {
    float m = clamp(1 - u, 0, 1);
    float m_square = m * m;
    return m_square * m_square * m;
}

float GTR1(float NdH, float a) {
    if (a >= 1.0) return invPI;
    float a2 = a * a;
    float t = 1.0 + (a2 - 1.0) * NdH * NdH;
    return (a2 - 1.0) / max(0.0001, PI * log(a2) * t);
}

float GTR2(float NdH, float a) {
    float a2 = a * a;
    float t = 1.0 + (a2 - 1.0) * NdH * NdH;
    return a2 / max(0.0001, PI * t * t);
}

float smithG_GGX(float NdV, float alphaG) {
    float a = alphaG * alphaG;
    float b = NdV * NdV;
    return 1.0 / max(0.0001, NdV + sqrt(a + b - a * b));
}

vec3 brdfEvaluate(vec3 V, vec3 N, vec3 L, in Material material) {
    float NdL = dot(N, L), NdV = dot(N, V);
    if (NdL < 0 || NdV < 0) return vec3(0.0);

    vec3 H = normalize(L + V);
    float NdH = dot(N, H), LdH = dot(L, H);

    vec3 Cdlin = material.plainColor;
    float Cdlum = max(0.0001, 0.299 * Cdlin.r + 0.587 * Cdlin.g + 0.114 * Cdlin.b); // 颜色转亮度
    vec3 Ctint = (Cdlum > 0) ? (Cdlin / Cdlum) : vec3(1.0);
    vec3 Cspec = material.specular * mix(vec3(1.0), Ctint, material.specularTint); // 色调偏转插值
    vec3 Cspec0 = mix(0.08 * Cspec, Cdlin, material.metallic); // F0
    vec3 Csheen = mix(vec3(1), Ctint, material.sheenTint);

    // 漫反射
    float rawFd = 2.0 * LdH * LdH * material.roughness + 0.5;
    float SFL = SchlickFresnel(NdL);
    float SFV = SchlickFresnel(NdV);
    float Fd = mix(1.0, rawFd, SFL) * mix(1.0, rawFd, SFV);

    // 次表面散射
    float rawFss = LdH * LdH * material.roughness;
    float Fss = mix(1.0, rawFss, SFL) * mix(1.0, rawFss, SFV);
    float NDLPNDV = NdL + NdV;
    if (abs(NDLPNDV) < 0.0001) NDLPNDV = 0.0001;
    float ss = 1.25 * (Fss * (1.0 / NDLPNDV - 0.5) + 0.5);

    // 镜面反射
    float alpha = material.roughness * material.roughness;
    float Ds = GTR2(NdH, alpha);
    float FH = SchlickFresnel(LdH);
    vec3 Fs = mix(Cspec0, vec3(1.0), FH);
    float Gs = smithG_GGX(NdL, material.roughness);
    Gs *= smithG_GGX(NdV, material.roughness);
    vec3 specular = Ds * Fs * Gs;

    // 清漆
    float Dr = GTR1(NdH, mix(0.1, 0.0001, material.clearcoatGloss));
    float Fr = mix(0.04, 1.0, FH);
    float Gr = smithG_GGX(NdL, 0.25) * smithG_GGX(NdV, 0.25);
    vec3 clearcoat = vec3(0.25 * Gr * Fr * Dr * material.clearcoat);

    // 织物
    vec3 Fsheen = FH * material.sheen * Csheen;

    vec3 diffuse = (1.0 / PI) * mix(Fd, ss, material.subsurface) * Cdlin + Fsheen;
    return diffuse * (1.0 - material.metallic) + specular + clearcoat;
}

vec3 SampleGTR2(float xi_1, float xi_2, vec3 V, vec3 N, float alpha) {
    float phi_h = 2.0 * PI * xi_1;
    float sin_phi_h = sin(phi_h);
    float cos_phi_h = cos(phi_h);
    float cos_theta_h = sqrt((1.0 - xi_2) / max(0.0001, 1.0 + (alpha * alpha - 1.0) * xi_2));
    float sin_theta_h = sqrt(max(0.0, 1.0 - cos_theta_h * cos_theta_h));
    vec3 H = vec3(sin_theta_h * cos_phi_h, sin_theta_h * sin_phi_h, cos_theta_h);
    H = hemisphereToNormal(H, N);
    vec3 L = reflect(-V, H);
    return L;
}

vec3 SampleGTR1(float xi_1, float xi_2, vec3 V, vec3 N, float alpha) {
    float phi_h = 2.0 * PI * xi_1;
    float sin_phi_h = sin(phi_h);
    float cos_phi_h = cos(phi_h);
    float cos_theta_h = sqrt((1.0 - pow(alpha * alpha, 1.0 - xi_2)) / max(0.0001, 1.0 - alpha * alpha));
    float sin_theta_h = sqrt(max(0.0, 1.0 - cos_theta_h * cos_theta_h));
    vec3 H = vec3(sin_theta_h*cos_phi_h, sin_theta_h*sin_phi_h, cos_theta_h);
    H = hemisphereToNormal(H, N);
    vec3 L = reflect(-V, H);
    return L;
}

vec3 SampleBRDF(float xi_1, float xi_2, float xi_3, vec3 V, vec3 N, in Material material) {
    float alpha_GTR1 = mix(0.1, 0.0001, material.clearcoatGloss);
    float alpha_GTR2 = max(0.0001, sqr(material.roughness));
    float r_diffuse = (1.0 - material.metallic);
    float r_specular = 1.0;
    float r_clearcoat = 0.25 * material.clearcoat;
    float r_sum = max(0.0001, r_diffuse + r_specular + r_clearcoat);
    float p_diffuse = r_diffuse / r_sum;
    float p_specular = r_specular / r_sum;
    float p_clearcoat = r_clearcoat / r_sum;
    float rd = xi_3;
    if(rd <= p_diffuse) {
        return cosineHemisphereSampler(xi_1, xi_2, N);
    } else if (p_diffuse < rd && rd <= p_diffuse + p_specular) {
        return SampleGTR2(xi_1, xi_2, V, N, alpha_GTR2);
    } else if (p_diffuse + p_specular < rd) {
        return SampleGTR1(xi_1, xi_2, V, N, alpha_GTR1);
    }
    return vec3(0.0, 1.0, 0.0);
}

float BRDF_Pdf(vec3 V, vec3 N, vec3 L, in Material material) {
    float NdL = dot(N, L);
    float NdV = dot(N, V);
    if(NdL < 0 || NdV < 0) return 0;
    vec3 H = normalize(L + V);
    float NdH = dot(N, H);
    if (abs(NdH) < 0.0001) NdH = 0.0001;
    float LdH = dot(L, H);
    if (abs(LdH) < 0.0001) LdH = 0.0001;
    float alpha = max(0.0001, sqr(material.roughness));
    float Ds = GTR2(NdH, alpha);
    float Dr = GTR1(NdH, mix(0.1, 0.0001, material.clearcoatGloss));
    float pdf_diffuse = NdL / PI;
    float pdf_specular = Ds * NdH / (4.0 * LdH);
    float pdf_clearcoat = Dr * NdH / (4.0 * LdH);
    float r_diffuse = (1.0 - material.metallic);
    float r_specular = 1.0;
    float r_clearcoat = 0.25 * material.clearcoat;
    float r_sum = max(0.0001, r_diffuse + r_specular + r_clearcoat);
    float p_diffuse = r_diffuse / r_sum;
    float p_specular = r_specular / r_sum;
    float p_clearcoat = r_clearcoat / r_sum;
    float pdf = p_diffuse * pdf_diffuse + p_specular * pdf_specular + p_clearcoat * pdf_clearcoat;
    return pdf;
}

float mixWeight(float a, float b) {
    float t = a * a;
    return t / (b * b + t);
}

// ================================== 主体 ================================== //

// vec3 tracing(HitRst hit, int maxDepth) {
//     vec3 Lo = vec3(0.0);
//     vec3 accColor = vec3(1.0);
//
//     for (int dep = 0; dep < maxDepth; dep ++) {
//         Ray ranRay;
//         ranRay.origin = hit.hitPoint;
//
//         #if ENABLE_SOBOL
//             vec2 uv = sobolVec2(frameCnt + 1, dep);
//             uv = CranleyPattersonRotation(uv);
//             vec3 rdt = hemisphereSampler(uv.x, uv.y);
//         #else
//             vec3 rdt = hemisphereSampler();
//         #endif
//
//         ranRay.direction = hemisphereToNormal(rdt, hit.normal);
//
//         HitRst nxtHit = hitBVHTree(ranRay);
//         HitRst dyHits = hitInterval(ranRay, 0, dytrisNum - 1, dytris);
//         if (dyHits.isHit && dyHits.t < nxtHit.t) nxtHit = dyHits;
//
//         float pdf = 1.0 / (2.0 * PI);
//
//         float cosO = max(0, dot(-hit.dir, hit.normal));
//         float cosI = max(0, dot(ranRay.direction, hit.normal));
//         vec3 f_r = brdfEvaluate(-hit.dir, hit.normal, ranRay.direction, hit.material); // hit.material.plainColor / PI;
//
//         if (!nxtHit.isHit) {
//             vec3 bkColor = vec3(1.0); // bkSampler(ranRay.direction);
//             Lo += accColor * bkColor * f_r * cosI / pdf;
//             break;
//         }
//
//         vec3 Le = nxtHit.material.emissive;
//         Lo += accColor * Le * f_r * cosI / pdf;
//
//         hit = nxtHit;
//         accColor *= f_r * cosI / pdf;
//     }
//
//     return Lo;
// }

vec3 tracingImportanceSampling(HitRst hit, int maxBounce) {
    vec3 Lo = vec3(0);
    vec3 history = vec3(1);

    for(int bounce = 0; bounce < maxBounce; bounce ++) {
        vec3 V = -hit.dir;
        vec3 N = hit.normal;

        Ray hdrRay;
        hdrRay.origin = hit.hitPoint;
        hdrRay.direction = bkSampler(rand(), rand());
        if (dot(N, hdrRay.direction) > 0.0) {
            HitRst hdrHit = hitBVHTree(hdrRay, tree, triangles);
            HitRst dyHits = hitBVHTree(hdrRay, dytree, dytris);
            if (dyHits.isHit && dyHits.t < hdrHit.t) hdrHit = dyHits;
            if (!hdrHit.isHit) {
                vec3 L = hdrRay.direction;
                vec3 color = hdrColor(L);
                float pdfLight = hdrPdf(L, hdrRes);
                pdfLight = max(0.0001, pdfLight);
                vec3 f_r = brdfEvaluate(V, N, L, hit.material);
                float pdf_brdf = BRDF_Pdf(V, N, L, hit.material);

                float weight = mixWeight(pdfLight, pdf_brdf);
                Lo += weight * history * color * f_r * dot(N, L) / pdfLight;
            }
        }

        vec2 uv = sobolVec2(frameCnt + 1, bounce);
        uv = CranleyPattersonRotation(uv);
        float xi_1 = uv.x, xi_2 = uv.y, xi_3 = rand();

        vec3 L = SampleBRDF(xi_1, xi_2, xi_3, V, N, hit.material);
        float NdL = dot(N, L);
        if (NdL <= 0.0) break;

        Ray randomRay;
        randomRay.origin = hit.hitPoint;
        randomRay.direction = L;
        HitRst newHit = hitBVHTree(randomRay, tree, triangles);
        HitRst dyHits = hitBVHTree(randomRay, dytree, dytris);
//         HitRst dyHits = hitInterval(randomRay, 0, dytrisNum - 1, dytris);
        if (dyHits.isHit && dyHits.t < newHit.t) newHit = dyHits;

        vec3 f_r = brdfEvaluate(V, N, L, hit.material);
        float pdf_brdf = BRDF_Pdf(V, N, L, hit.material);
        if (pdf_brdf <= 0.0) break;
        pdf_brdf = max(0.0001, pdf_brdf);

        if(!newHit.isHit) {
            vec3 color;
            if (hdrClosed == 1 || chessSpClosed == 1) {
                color = vec3(1.0);
            } else {
                color = hdrColor(L);
            }
            float pdfLight = hdrPdf(L, hdrRes);

            float weight = mixWeight(pdf_brdf, pdfLight);
            Lo += weight * history * color * f_r * NdL / pdf_brdf;
        }

        vec3 Le = newHit.material.emissive;
        Lo += history * Le * f_r * NdL / pdf_brdf;

        hit = newHit;
        history *= f_r * NdL / pdf_brdf;
    }

    return Lo;
}

void main() {
    vec3 color;
    bool flag = false;
    if (int((-pos.y + 1) / 2 * vres) <= 30) {
        for (int i = 0; !flag && i < pointNum; i ++) {
            vec2 t = texelFetch(surpnter, i).xy;
            if (int((pos.x + 1) / 2 * hres) == t.x && int((- pos.y + 1) / 2 * vres) == t.y) flag = true;
        }
    }
    float legals[8][8];
    for (int i = 0; i < 8; i ++) {
        for (int j = 0; j < 8; j ++) {
            legals[i][j] = texelFetch(lpinf, i * 8 + j).x;
        }
    }

    if (!flag) {
        Ray ray;
        ray.origin = eye;
        vec2 bias = 2 * vec2((rand() - 0.5) / float(hres), (rand() - 0.5) / float(vres));
        ray.direction = normalize(w + (pos.x + bias.x) * u + (-pos.y * vres / hres + bias.y) * v);

        HitRst oriHit = hitBVHTree(ray, tree, triangles);
        HitRst dyHits = hitBVHTree(ray, dytree, dytris);
//         HitRst dyHits = hitInterval(ray, 0, dytrisNum - 1, dytris);
        if (dyHits.isHit && dyHits.t < oriHit.t) oriHit = dyHits;

        if (!oriHit.isHit) {
            color = hdrColor(ray.direction);
        } else {
            vec3 Le = oriHit.material.emissive;
            if (TDEPTH == 0) Le = oriHit.material.plainColor + Le;
            vec3 Li = tracingImportanceSampling(oriHit, TDEPTH); // tracing(oriHit, 3);
            color = Le + Li;
            if (selected >= 0 && oriHit.material.tag == selected) color += vec3(0.75, 0.75, 1.8); // 鼠标选中
            if (lastpos >= 0 && oriHit.material.tag == lastpos) color += vec3(1.8, 0.75, 0.75); // 上次落子
            if (legals[oriHit.material.tag / 10][oriHit.material.tag % 10] > 0.5) color += vec3(0.75, 1.5, 0.75); // 可落子区域
        }

        vec3 lastColor = texture2D(lastFrame, pos.xy * 0.5 + 0.5).rgb;
        color = mix(lastColor, color, 1.0 / float(frameCnt + 1));
    } else {
        color = vec3(10.0);
    }

    gl_FragData[0] = vec4(color, 1.0);
}
